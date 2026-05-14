# 云犀本地 AI 服务

本目录提供一个本地 FastAPI 服务，用来模拟企业级 embedding + rerank 模型网关，供云犀平台开发、演示、RAG 链路验证和面试讲解使用。

本服务不是生产高并发推理服务。生产环境应替换为 GPU 推理服务、企业模型网关或商业模型 API；Java 侧通过 `EmbeddingClient` / `RerankClient` Provider 抽象完成切换。

## 默认模型

```text
embedding: BAAI/bge-small-zh-v1.5
reranker: cross-encoder/mmarco-mMiniLMv2-L6-H384-v1
```

说明：

- `BAAI/bge-small-zh-v1.5`：轻量中文 embedding 模型，适合本地中文知识库向量化和 RAG 演示。
- `cross-encoder/mmarco-mMiniLMv2-L6-H384-v1`：轻量多语言 cross-encoder reranker，适合本地模拟中文 rerank 链路。

## 目录文件

```text
local-ai-service
├─ ai_service.py      # FastAPI 服务入口
├─ requirements.txt   # Python 依赖
├─ .env               # 本地启动配置，含中文注释
└─ README.md          # 当前说明文档
```

## WSL 安装

进入服务目录：

```bash
cd /mnt/d/LLYWORK/lly-project/YunXi/YunXi-Agent-Platform/script/server/local-ai-service
```

### 方式一：使用 Miniconda / Conda（推荐）

如果 WSL 已安装 Miniconda，推荐为本地 AI 服务单独创建一个 conda 环境，避免污染 `base` 环境。

创建环境：

```bash
conda create -n yunxi-ai-service python=3.10 -y
```

激活环境：

```bash
conda activate yunxi-ai-service
```

安装依赖：

```bash
pip install torch --index-url https://download.pytorch.org/whl/cpu
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

后续每次启动服务前，先进入目录并激活环境：

```bash
cd /mnt/d/LLYWORK/lly-project/YunXi/YunXi-Agent-Platform/script/server/local-ai-service
conda activate yunxi-ai-service
```

如果需要退出环境：

```bash
conda deactivate
```

### 方式二：使用 Python venv

如果不想使用 conda，也可以创建普通 venv 虚拟环境。

创建并激活虚拟环境：

```bash
python -m venv .venv-ai
source .venv-ai/bin/activate
```

安装依赖：

```bash
pip install torch --index-url https://download.pytorch.org/whl/cpu
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

如果你已经有专门的 Python 环境，也可以直接使用已有环境，不必创建 `.venv-ai`。

## 启动服务

### 后台启动（推荐）

推荐在 WSL 中使用 `.env` + `nohup` 后台启动服务。监听地址、端口和 worker 数量使用固定推荐值：

```text
host: 0.0.0.0
port: 18080
workers: 1
```

```bash
# 如果 .env 是从 Windows 创建或编辑的，先转换 CRLF 换行为 Linux LF。
# 否则 source .env 后变量可能携带不可见的 \r，导致模型名等参数异常。
sed -i 's/\r$//' .env

# 开启自动导出变量模式。
# 后面 source .env 读取到的变量会自动成为环境变量，Python 可以通过 os.getenv(...) 读取。
set -a

# 读取当前目录下的 .env 文件。
# 会加载 TORCH_NUM_THREADS、EMBEDDING_MODEL、RERANK_MODEL 等模型服务配置。
source .env

# 关闭自动导出变量模式，避免后续普通 shell 变量被自动导出。
set +a

# 后台启动 FastAPI 服务。
# nohup：终端关闭后服务继续运行。
# uvicorn ai_service:app：启动 ai_service.py 中的 FastAPI app。
# --host 0.0.0.0：监听所有网卡，方便 Windows 侧访问 WSL 服务。
# --port 18080：固定本地 AI 服务端口。
# --workers 1：本机推荐 1 个 worker，避免重复加载模型占用内存。
# > ai_service.log：标准输出写入日志文件。
# 2>&1：错误输出也写入同一个日志文件。
# &：放到后台运行。
nohup uvicorn ai_service:app --host 0.0.0.0 --port 18080 --workers 1 > ai_service.log 2>&1 &
```

也可以直接用一行命令后台启动：

```bash
sed -i 's/\r$//' .env && set -a && source .env && set +a && nohup uvicorn ai_service:app --host 0.0.0.0 --port 18080 --workers 1 > ai_service.log 2>&1 &
```

查看日志：

```bash
# 实时查看服务日志。
tail -f ai_service.log
```

查看进程：

```bash
# 查看 uvicorn 服务进程。
ps -ef | grep uvicorn
```

停止服务：

```bash
# 停止当前 ai_service 对应的 uvicorn 进程。
pkill -f "uvicorn ai_service:app"
```

如果你希望更谨慎，可以先查看 PID 再停止：

```bash
# 找到 uvicorn 进程 PID。
ps -ef | grep uvicorn

# 停止指定 PID。
kill <PID>
```

### 前台启动（调试用）

前台启动会占用当前终端，日志直接输出在终端里。按 `Ctrl + C` 会停止服务。

```bash
# 如果 .env 是从 Windows 创建或编辑的，先转换 CRLF 换行为 Linux LF。
sed -i 's/\r$//' .env

# 开启自动导出变量模式。
set -a

# 读取 .env 配置。
source .env

# 关闭自动导出变量模式。
set +a

# 前台启动服务，适合首次调试或观察模型加载日志。
uvicorn ai_service:app --host 0.0.0.0 --port 18080 --workers 1
```

也可以不使用 `.env`，手动指定环境变量后前台启动：

```bash
# 设置 PyTorch CPU 线程数。
export TORCH_NUM_THREADS=4

# 设置 embedding 模型。
export EMBEDDING_MODEL=BAAI/bge-small-zh-v1.5

# 设置 reranker 模型。
export RERANK_MODEL=cross-encoder/mmarco-mMiniLMv2-L6-H384-v1

# 设置 embedding 批大小。
export EMBEDDING_BATCH_SIZE=32

# 设置 rerank 批大小。
export RERANK_BATCH_SIZE=8

# 前台启动服务。
uvicorn ai_service:app --host 0.0.0.0 --port 18080 --workers 1
```

建议：

- `workers=1`：推荐本机默认值。
- `workers=2`：会加载两份 embedding/rerank 模型，占用两份内存，仅在内存足够且确有需要时尝试。
- `RERANK_BATCH_SIZE=4~8`：reranker 比 embedding 更耗 CPU，响应慢时优先调小。

## 健康检查

```bash
curl http://localhost:18080/health
curl http://localhost:18080/models
```

返回示例：

```json
{
  "status": "ok",
  "embedding_model": "BAAI/bge-small-zh-v1.5",
  "rerank_model": "cross-encoder/mmarco-mMiniLMv2-L6-H384-v1"
}
```

## 测试 embedding

```bash
curl -X POST http://localhost:18080/embedding \
  -H "Content-Type: application/json" \
  -d '{"texts":["客户投诉套餐变更后资费不一致","5G畅享套餐包含国内通用流量"]}'
```

返回内容包含：

```text
model       当前 embedding 模型
dim         向量维度
count       文本数量
elapsed_ms  推理耗时
vectors     向量结果
```

## 测试 rerank

```bash
curl -X POST http://localhost:18080/rerank \
  -H "Content-Type: application/json" \
  -d '{"query":"客户投诉套餐变更后资费不一致怎么办","documents":["客户可通过营业厅查询套餐变更记录和生效时间","北京今天下雨","重复投诉客户需要识别升级风险并跟进工单"],"top_n":2}'
```

返回内容包含：

```text
model            当前 reranker 模型
query            查询文本
top_n            返回数量
candidate_count  候选文档数量
elapsed_ms       推理耗时
results          精排结果，按 score 倒序
```

## Java 接入配置示例

本地开发配置示例：

```yaml
yunxi:
  ai:
    embedding:
      provider: local
      base-url: http://localhost:18080
      model: BAAI/bge-small-zh-v1.5
      timeout: 5s
      max-concurrency: 20
    rerank:
      provider: local
      base-url: http://localhost:18080
      model: cross-encoder/mmarco-mMiniLMv2-L6-H384-v1
      timeout: 8s
      max-concurrency: 10
      fallback-enabled: true
```

生产环境可以在不改 Agent/RAG 主流程的情况下替换 Provider：

```text
本地 FastAPI 服务 -> 企业模型网关 -> GPU 推理服务 / 商业模型 API
```

## 面试讲解口径

可以这样讲：

> 本地开发环境使用轻量 embedding/rerank 服务模拟企业模型网关。Java 业务服务不直接加载模型，而是通过 `EmbeddingClient` 和 `RerankClient` Provider 抽象调用模型能力。生产环境可替换为 GPU 推理服务或统一模型网关。Java 侧负责连接池、超时、限流、熔断、降级和 trace 观测。

重点表达：

- 本地模型服务用于验证 RAG 链路，不宣称生产高并发。
- 生产高并发通过独立模型服务多副本、GPU 推理集群或企业模型网关承载。
- Java 侧保留 Provider 抽象，可平滑切换模型服务。
- rerank 超时可降级为向量召回顺序。
- embedding 失败可降级为关键词检索或返回可追踪错误。
- 每次 embedding/rerank 调用都应记录 provider、model、topK、topN、elapsedMs、status、errorType。
