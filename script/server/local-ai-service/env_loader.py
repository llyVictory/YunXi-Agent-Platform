from pathlib import Path

from dotenv import load_dotenv


def load_local_dotenv() -> None:
    env_path = Path(__file__).with_name(".env")
    load_dotenv(dotenv_path=env_path, override=False)
