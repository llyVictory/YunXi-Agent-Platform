import os
import time
from typing import List

import torch
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from sentence_transformers import CrossEncoder, SentenceTransformer


def _env_str(name: str, default: str) -> str:
    value = os.getenv(name, default)
    return value.strip().strip("'").strip('"')


EMBEDDING_MODEL_NAME = _env_str("EMBEDDING_MODEL", "BAAI/bge-small-zh-v1.5")
RERANK_MODEL_NAME = _env_str(
    "RERANK_MODEL",
    "cross-encoder/mmarco-mMiniLMv2-L6-H384-v1",
)
TORCH_NUM_THREADS = int(os.getenv("TORCH_NUM_THREADS", "4"))
EMBEDDING_BATCH_SIZE = int(os.getenv("EMBEDDING_BATCH_SIZE", "32"))
RERANK_BATCH_SIZE = int(os.getenv("RERANK_BATCH_SIZE", "8"))
MAX_EMBEDDING_TEXTS = int(os.getenv("MAX_EMBEDDING_TEXTS", "64"))
MAX_RERANK_DOCUMENTS = int(os.getenv("MAX_RERANK_DOCUMENTS", "50"))
MAX_TEXT_LENGTH = int(os.getenv("MAX_TEXT_LENGTH", "2000"))

torch.set_num_threads(TORCH_NUM_THREADS)

app = FastAPI(
    title="YunXi Local Embedding + Reranker Service",
    description=(
        "Local development service that simulates an enterprise model gateway "
        "for embedding and rerank calls."
    ),
)

embedding_model = SentenceTransformer(EMBEDDING_MODEL_NAME)
reranker_model = CrossEncoder(RERANK_MODEL_NAME, max_length=512)


class EmbeddingRequest(BaseModel):
    texts: List[str] = Field(..., min_length=1)


class RerankRequest(BaseModel):
    query: str = Field(..., min_length=1)
    documents: List[str] = Field(..., min_length=1)
    top_n: int = Field(default=5, ge=1)


def _validate_texts(texts: List[str], max_items: int, field_name: str) -> None:
    if len(texts) > max_items:
        raise HTTPException(
            status_code=400,
            detail=f"{field_name} size exceeds limit {max_items}",
        )
    for index, text in enumerate(texts):
        if text is None or not text.strip():
            raise HTTPException(
                status_code=400,
                detail=f"{field_name}[{index}] must not be blank",
            )
        if len(text) > MAX_TEXT_LENGTH:
            raise HTTPException(
                status_code=400,
                detail=f"{field_name}[{index}] length exceeds limit {MAX_TEXT_LENGTH}",
            )


@app.get("/health")
def health():
    return {
        "status": "ok",
        "embedding_model": EMBEDDING_MODEL_NAME,
        "rerank_model": RERANK_MODEL_NAME,
    }


@app.get("/models")
def models():
    return {
        "embedding": {
            "model": EMBEDDING_MODEL_NAME,
            "batch_size": EMBEDDING_BATCH_SIZE,
            "max_texts": MAX_EMBEDDING_TEXTS,
        },
        "rerank": {
            "model": RERANK_MODEL_NAME,
            "batch_size": RERANK_BATCH_SIZE,
            "max_documents": MAX_RERANK_DOCUMENTS,
        },
        "torch_num_threads": TORCH_NUM_THREADS,
        "max_text_length": MAX_TEXT_LENGTH,
    }


@app.post("/embedding")
def embedding(req: EmbeddingRequest):
    _validate_texts(req.texts, MAX_EMBEDDING_TEXTS, "texts")
    started = time.perf_counter()

    vectors = embedding_model.encode(
        req.texts,
        normalize_embeddings=True,
        batch_size=EMBEDDING_BATCH_SIZE,
        show_progress_bar=False,
    )

    elapsed_ms = int((time.perf_counter() - started) * 1000)
    return {
        "model": EMBEDDING_MODEL_NAME,
        "dim": len(vectors[0]),
        "count": len(vectors),
        "elapsed_ms": elapsed_ms,
        "vectors": vectors.tolist(),
    }


@app.post("/rerank")
def rerank(req: RerankRequest):
    if not req.query.strip():
        raise HTTPException(status_code=400, detail="query must not be blank")
    if len(req.query) > MAX_TEXT_LENGTH:
        raise HTTPException(
            status_code=400,
            detail=f"query length exceeds limit {MAX_TEXT_LENGTH}",
        )
    _validate_texts(req.documents, MAX_RERANK_DOCUMENTS, "documents")

    started = time.perf_counter()
    pairs = [[req.query, doc] for doc in req.documents]
    scores = reranker_model.predict(
        pairs,
        batch_size=RERANK_BATCH_SIZE,
        show_progress_bar=False,
    )

    results = [
        {
            "index": i,
            "document": doc,
            "score": float(score),
        }
        for i, (doc, score) in enumerate(zip(req.documents, scores))
    ]
    results.sort(key=lambda item: item["score"], reverse=True)

    elapsed_ms = int((time.perf_counter() - started) * 1000)
    top_n = min(req.top_n, len(results))
    return {
        "model": RERANK_MODEL_NAME,
        "query": req.query,
        "top_n": top_n,
        "candidate_count": len(req.documents),
        "elapsed_ms": elapsed_ms,
        "results": results[:top_n],
    }
