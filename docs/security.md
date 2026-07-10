# Security

## Authentication model

The application uses JWT bearer authentication with an RSA key pair.

- private key: signs tokens
- public key: verifies tokens

JWT claims include:
- `sub` — user ID
- `tenant_id` — tenant ID
- `role` — Spring Security role

## Authorization

Spring Security and method-level security are used together:
- `SecurityConfig` protects the HTTP endpoints
- `@PreAuthorize` guards controller operations by role

## Tenant resolution

Tenant-aware requests use the current JWT and request header information to resolve the active tenant.

Key classes:
- `JwtAuthenticationFilter`
- `TenantContext`
- `TenantSchemaResolver`
- `CurrentTenantIdentifierResolverImpl`
- `MultiTenantConnectionProviderImpl`

## Password handling

Passwords are hashed with BCrypt before storage.

Important notes:
- plaintext passwords are never stored
- `adminPassword` on the tenant entity is only used to create the initial tenant admin account
- API responses should not expose password fields

## Public endpoints

Public endpoints include:
- `/api/v1/auth/login`
- `/api/v1/auth/register`
- Swagger/OpenAPI documentation paths

Everything else requires authentication.

