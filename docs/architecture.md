# Architecture

## Overview

`MutliTenantSaas` is a Spring Boot 4 multi-tenant backend built around **schema-based multitenancy** in PostgreSQL.

The application uses:
- a shared `public` schema for platform-level data such as tenants and the bootstrap admin user,
- one PostgreSQL schema per tenant for business data,
- JWT authentication with RSA signing,
- Spring Security for authentication and authorization,
- Spring Data JPA auditing for creation and modification metadata,
- Flyway migrations for repeatable database setup,
- Swagger/OpenAPI for API documentation.

## Request flow

1. A client authenticates through `POST /api/v1/auth/login`.
2. `JwtTokenService` issues an RS256 JWT with the user ID, tenant ID, and role.
3. `JwtAuthenticationFilter` validates the bearer token on secured requests.
4. The filter resolves the tenant ID and uses `TenantSchemaResolver` to determine the PostgreSQL schema.
5. `TenantContext` stores the current tenant and schema for the duration of the request.
6. Hibernate uses `MultiTenantConnectionProviderImpl` and `CurrentTenantIdentifierResolverImpl` to route SQL to the correct schema.
7. Controllers call services, which in turn use repositories and mappers to read/write entities.

## Main packages

### `controller`
REST endpoints for authentication, tenant lifecycle, users, categories, products, and stock movements.

### `service`
Business logic for:
- authentication
- tenant registration and approval
- tenant provisioning
- user management
- category and product CRUD
- stock movement tracking
- password hashing

### `repository`
Spring Data JPA repositories for tenant and tenant-scoped entities.

### `entity`
JPA entities that map to the database schema.

### `dto`
Request/response contracts used by the API.

### `security`
JWT token generation and request authentication.

### `config`
Infrastructure configuration for multitenancy, auditing, caching, pagination, and OpenAPI.

### `mapper`
Entity-to-DTO conversion helpers.

## Multi-tenancy design

The platform follows a **schema-per-tenant** strategy:

- the shared schema stores `tenants` and global platform data,
- each approved tenant gets a schema named `tenant_<companyCode>`,
- the schema is created and migrated during tenant approval,
- tenant-scoped entities live in the tenant schema only.

This design isolates tenant data while still allowing a single application instance and a single PostgreSQL database.

## Important infrastructure classes

- `Application` enables JPA auditing and Spring Data web support.
- `TenantContext` stores the current tenant ID and schema in thread-local storage.
- `JwtAuthenticationFilter` resolves the authenticated user and tenant on every request.
- `TenantSchemaResolver` maps a tenant ID to a schema name.
- `MultiTenantConnectionProviderImpl` switches PostgreSQL `search_path` to the active schema.
- `CurrentTenantIdentifierResolverImpl` returns the schema Hibernate should use.
- `ProvisioningServiceImpl` creates a tenant schema and runs tenant Flyway migrations.
- `OpenApiConfig` defines the OpenAPI metadata and JWT security scheme.

