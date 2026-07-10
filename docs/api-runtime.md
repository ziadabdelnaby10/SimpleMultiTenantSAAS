# API Runtime

## Base URL

The application runs with a context path of:

- `/api`

So the local base URL becomes:

- `http://localhost:8080/api`

## Authentication

### Login
`POST /api/v1/auth/login`

Returns a JWT access token.

### Tenant registration
`POST /api/v1/auth/register`

Creates a new tenant in `PENDING` status.

## Protected endpoints

Most endpoints require:
- `Authorization: Bearer <JWT>`

Tenant-scoped endpoints also rely on:
- `X-Tenant-ID: <tenant-id>`

## Pagination

Collection endpoints return `Page<T>` responses and accept standard Spring Data pagination query parameters:
- `page`
- `size`
- `sort`

Default page size is `20`.
Maximum page size is capped at `100` by `WebConfig`.

## Main resources

### Tenants
- `GET /api/v1/tenants`
- `PATCH /api/v1/tenants/approve/{tenantId}`
- `PATCH /api/v1/tenants/activate/{tenantId}`
- `PATCH /api/v1/tenants/deactivate/{tenantId}`
- `PATCH /api/v1/tenants/suspend/{tenantId}`

### Users
- `POST /api/v1/users`
- `GET /api/v1/users`
- `GET /api/v1/users/{userId}`
- `PUT /api/v1/users/{userId}`
- `DELETE /api/v1/users/{userId}`
- `PATCH /api/v1/users/{userId}/enable`
- `PATCH /api/v1/users/{userId}/disable`

### Categories
- `POST /api/v1/categories`
- `GET /api/v1/categories`
- `GET /api/v1/categories/{categoryId}`
- `PUT /api/v1/categories/{categoryId}`
- `DELETE /api/v1/categories/{categoryId}`

### Products
- `POST /api/v1/products`
- `GET /api/v1/products`
- `GET /api/v1/products/{productId}`
- `PUT /api/v1/products/{productId}`
- `DELETE /api/v1/products/{productId}`

### Stock movements
- `POST /api/v1/stocks`
- `GET /api/v1/stocks`
- `GET /api/v1/stocks/{stockId}`
- `GET /api/v1/stocks/product/{productId}`
- `PUT /api/v1/stocks/{stockId}`
- `DELETE /api/v1/stocks/{stockId}`

## Swagger UI

Swagger/OpenAPI is available at:
- `/api/swagger-ui.html`
- `/api/v3/api-docs`

The documentation describes:
- request/response schemas
- validation constraints
- security requirements
- pagination parameters
- HTTP status codes

## Typical usage flow

1. Register a tenant.
2. Approve the tenant as a platform admin.
3. Log in with a tenant user.
4. Include the JWT in the `Authorization` header for future calls.
5. Include `X-Tenant-ID` on tenant-scoped requests.

