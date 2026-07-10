# MutliTenantSaas

A Spring Boot 4 multi-tenant SaaS backend built with schema-based PostgreSQL tenancy, JWT authentication, Flyway migrations, JPA auditing, validation, and Swagger/OpenAPI documentation.

## Quick Links

- [Architecture](docs/architecture.md)
- [Database Schema](docs/database-schema.md)
- [API Runtime](docs/api-runtime.md)
- [Local Development](docs/local-development.md)
- [Docker Setup](docs/docker-setup.md)
- [Security](docs/security.md)
- [Postman Usage](docs/postman-usage.md)

## Tech Stack

- Java 21
- Spring Boot 4.0.7
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- Flyway
- PostgreSQL 17.5
- springdoc-openapi 3.0.2
- JWT with jjwt 0.12.6
- Maven

## What the project does

- registers new tenants in a pending state
- approves tenants and provisions a dedicated PostgreSQL schema
- authenticates users with RSA-signed JWT tokens
- isolates tenant data using schema-based multitenancy
- manages users, categories, products, and stock movements inside tenant schemas
- exposes Swagger UI and OpenAPI specs for the REST API

## Running locally

1. Start PostgreSQL.
2. Configure environment variables in `.env`.
3. Run the application with Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

## API URLs

- Base URL: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`

## Docker

The repository includes Docker support for PostgreSQL and the Spring Boot application.

```bash
docker compose up -d
```

## Documentation

Detailed documentation lives under `docs/`.


