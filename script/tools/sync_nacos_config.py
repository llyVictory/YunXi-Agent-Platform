# 作用：
#   将 script/config/nacos 目录下的配置文件同步到 Nacos。
#
# 环境：
#   conda activate base
#   pip install -r script/tools/requirements.txt
#
# 常用命令：
#   只预览要同步哪些配置，不写入 Nacos：
#     python script/tools/sync_nacos_config.py --dry-run --no-watch
#
#   全量同步一次后退出，会写入 Nacos：
#     python script/tools/sync_nacos_config.py --no-watch
#
#   全量同步一次，然后持续监听本地配置变化并自动写入 Nacos：
#     python script/tools/sync_nacos_config.py

from __future__ import annotations

import argparse
import hashlib
import time
from dataclasses import dataclass
from pathlib import Path

import requests


# 默认同步当前项目的 Nacos 配置目录。
DEFAULT_CONFIG_DIR = Path(__file__).resolve().parents[1] / "config" / "nacos"

# 默认目标 Nacos 环境。
DEFAULT_NACOS_SERVER = "http://10.72.100.125:8848"
DEFAULT_NAMESPACE = "dev"
DEFAULT_GROUP = "DEFAULT_GROUP"
DEFAULT_USERNAME = "nacos"
DEFAULT_PASSWORD = "nacos"

# README 不是 Nacos 配置，跳过。
IGNORED_FILES = {"README.md"}

# 只同步 Nacos 常见配置文件类型。
SUPPORTED_SUFFIXES = {".yml", ".yaml", ".properties", ".json", ".xml"}


@dataclass(frozen=True)
class AppOptions:
    """命令行参数解析后的运行选项。"""

    config_dir: Path
    nacos_server: str
    namespace: str
    group: str
    username: str
    password: str
    dry_run: bool
    no_watch: bool
    skip_initial_sync: bool
    interval_seconds: float


@dataclass(frozen=True)
class ConfigFile:
    """本地配置文件在 Nacos 中的映射。"""

    path: Path
    data_id: str
    config_type: str
    fingerprint: str


class NacosClient:
    """封装 Nacos 登录与配置发布 API。"""

    def __init__(self, options: AppOptions) -> None:
        self.options = options
        self.base_url = options.nacos_server.rstrip("/")
        self.access_token: str | None = None

    def login(self) -> None:
        """登录 Nacos，获取 accessToken。关闭鉴权的 Nacos 会自动降级为无 token。"""
        if not self.options.username:
            return

        url = f"{self.base_url}/nacos/v1/auth/users/login"
        payload = {
            "username": self.options.username,
            "password": self.options.password,
        }

        try:
            response = requests.post(url, data=payload, timeout=10)
            response.raise_for_status()
        except requests.RequestException as exc:
            print(f"[warn] Nacos login failed, continue without token: {exc}")
            return

        data = response.json()
        self.access_token = data.get("accessToken")

    def publish(self, config_file: ConfigFile) -> None:
        """发布单个本地配置文件到 Nacos。"""
        if self.options.dry_run:
            print_publish_result("dry-run", config_file, self.options)
            return

        url = f"{self.base_url}/nacos/v1/cs/configs"
        payload = {
            "dataId": config_file.data_id,
            "group": self.options.group,
            "tenant": self.options.namespace,
            "type": config_file.config_type,
            "content": config_file.path.read_text(encoding="utf-8"),
        }

        if self.access_token:
            payload["accessToken"] = self.access_token

        response = requests.post(url, data=payload, timeout=10)
        response.raise_for_status()
        print_publish_result(response.text, config_file, self.options)


def parse_options() -> AppOptions:
    parser = argparse.ArgumentParser(
        description="Sync local script/config/nacos files to Nacos."
    )
    parser.add_argument("--config-dir", default=str(DEFAULT_CONFIG_DIR))
    parser.add_argument("--server", default=DEFAULT_NACOS_SERVER)
    parser.add_argument("--namespace", default=DEFAULT_NAMESPACE)
    parser.add_argument("--group", default=DEFAULT_GROUP)
    parser.add_argument("--username", default=DEFAULT_USERNAME)
    parser.add_argument("--password", default=DEFAULT_PASSWORD)
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--no-watch", action="store_true")
    parser.add_argument("--skip-initial-sync", action="store_true")
    parser.add_argument("--interval", type=float, default=1.0)

    args = parser.parse_args()
    config_dir = Path(args.config_dir).resolve()
    if not config_dir.exists():
        raise SystemExit(f"Config directory does not exist: {config_dir}")

    return AppOptions(
        config_dir=config_dir,
        nacos_server=args.server,
        namespace=args.namespace,
        group=args.group,
        username=args.username,
        password=args.password,
        dry_run=args.dry_run,
        no_watch=args.no_watch,
        skip_initial_sync=args.skip_initial_sync,
        interval_seconds=args.interval,
    )


def list_config_files(config_dir: Path) -> list[Path]:
    """列出需要同步到 Nacos 的本地配置文件。"""
    files: list[Path] = []
    for path in sorted(config_dir.iterdir()):
        if not path.is_file():
            continue
        if path.name in IGNORED_FILES:
            continue
        if path.suffix.lower() not in SUPPORTED_SUFFIXES:
            continue
        files.append(path)
    return files


def build_config_file(path: Path) -> ConfigFile:
    """将本地路径转换为 Nacos 配置对象。"""
    return ConfigFile(
        path=path,
        data_id=path.name,
        config_type=detect_config_type(path),
        fingerprint=build_fingerprint(path),
    )


def detect_config_type(path: Path) -> str:
    """根据文件后缀推断 Nacos 配置类型。"""
    suffix = path.suffix.lower()
    if suffix in {".yml", ".yaml"}:
        return "yaml"
    if suffix == ".properties":
        return "properties"
    if suffix == ".json":
        return "json"
    if suffix == ".xml":
        return "xml"
    return "text"


def build_fingerprint(path: Path) -> str:
    """生成文件指纹，用于判断文件内容是否变化。"""
    content_hash = hashlib.sha256(path.read_bytes()).hexdigest()
    stat = path.stat()
    return f"{stat.st_mtime_ns}:{stat.st_size}:{content_hash}"


def load_snapshot(config_dir: Path) -> dict[Path, ConfigFile]:
    """读取目录当前状态。"""
    return {
        path: build_config_file(path)
        for path in list_config_files(config_dir)
    }


def sync_all(client: NacosClient, config_dir: Path) -> dict[Path, ConfigFile]:
    """全量同步配置目录。"""
    snapshot = load_snapshot(config_dir)
    for config_file in snapshot.values():
        client.publish(config_file)
    return snapshot


def watch_changes(
    client: NacosClient,
    config_dir: Path,
    known_files: dict[Path, ConfigFile],
    interval_seconds: float,
) -> None:
    """轮询目录变化，并把新增或修改的文件发布到 Nacos。"""
    print("Watching config changes. Press Ctrl+C to stop.")

    while True:
        time.sleep(interval_seconds)
        current_files = load_snapshot(config_dir)

        publish_changed_files(client, current_files, known_files)
        forget_removed_files(current_files, known_files)


def publish_changed_files(
    client: NacosClient,
    current_files: dict[Path, ConfigFile],
    known_files: dict[Path, ConfigFile],
) -> None:
    """发布新增或内容变化的配置文件。"""
    for path, current in current_files.items():
        previous = known_files.get(path)
        if previous and previous.fingerprint == current.fingerprint:
            continue

        try:
            client.publish(current)
            known_files[path] = current
        except requests.RequestException as exc:
            print(f"[warn] publish failed for {current.data_id}: {exc}")


def forget_removed_files(
    current_files: dict[Path, ConfigFile],
    known_files: dict[Path, ConfigFile],
) -> None:
    """本地删除文件时只从监听快照移除，不主动删除 Nacos 远端配置。"""
    removed_paths = set(known_files) - set(current_files)
    for path in removed_paths:
        print(f"[info] local file removed, skip deleting remote config: {path.name}")
        known_files.pop(path, None)


def print_startup_info(options: AppOptions) -> None:
    print("Nacos config sync")
    print(f"ConfigDir : {options.config_dir}")
    print(f"Server    : {options.nacos_server}")
    print(f"Namespace : {options.namespace}")
    print(f"Group     : {options.group}")


def print_publish_result(
    result: str,
    config_file: ConfigFile,
    options: AppOptions,
) -> None:
    timestamp = time.strftime("%H:%M:%S")
    print(
        f"[{timestamp}] {result} {config_file.data_id} "
        f"namespace={options.namespace} group={options.group}"
    )


def main() -> None:
    options = parse_options()
    print_startup_info(options)

    client = NacosClient(options)
    if not options.dry_run:
        client.login()

    if options.skip_initial_sync:
        known_files = load_snapshot(options.config_dir)
    else:
        known_files = sync_all(client, options.config_dir)

    if not options.no_watch:
        watch_changes(
            client=client,
            config_dir=options.config_dir,
            known_files=known_files,
            interval_seconds=options.interval_seconds,
        )


if __name__ == "__main__":
    main()
