# YunXi Local Server Scripts

This directory contains local auxiliary services used for YunXi development, demo, and interview validation.

Each service should live in its own subdirectory so future local services can be added without mixing dependencies and startup commands.

## Services

| Directory | Purpose | Default Port |
| --- | --- | --- |
| `local-ai-service` | Local embedding and rerank model gateway simulator | `18080` |

## Conventions

- Keep each service self-contained with its own README and dependency file.
- Do not put secrets, API keys, or production credentials in scripts.
- Local services are for development and validation only. Production should use Nacos configuration and replace local endpoints with enterprise services or model gateways.
