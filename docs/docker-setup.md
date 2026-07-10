# Docker Setup

## Overview

The repository includes PostgreSQL via `docker-compose.yml`.
This setup can be extended to run the Spring Boot application in a container as well.

## Services

### PostgreSQL
- image: `postgres:17.5`
- container name: `saas-db-container`
- host port: `${DB_PORT:-5434}`
- container port: `5432`
- volume: `postgres_data`

### Application
Use the root `Dockerfile` to build the Spring Boot application image.

## Build the app image

```bash
docker build -t mutli-tenant-saas-app .
```

## Run the stack

A common workflow is:

1. Start PostgreSQL with Docker Compose.
2. Build and run the application container.

Example environment values inside Docker:
- `DB_HOST=postgres`
- `DB_PORT=5432`
- `DB_NAME=saas-app-db`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
- `SERVER_PORT=8080`

## Recommended full compose setup

A production-friendly compose file should include:
- `postgres`
- `app`

The application service should depend on the database service and connect to it using the service name `postgres`.

