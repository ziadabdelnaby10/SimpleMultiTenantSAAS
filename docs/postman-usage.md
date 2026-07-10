# Postman Usage

The repository contains a Postman collection at:

- `docs/Multi-Tenant SaaS API.postman_collection.json`

## Suggested workflow

1. Import the collection into Postman.
2. Configure the base URL to `http://localhost:8080/api`.
3. Log in to obtain a JWT token.
4. Set the `Authorization` header to `Bearer <token>`.
5. Set `X-Tenant-ID` for tenant-scoped endpoints.

## Collection coverage

The collection can be used to test:
- authentication
- tenant registration and approval
- users
- categories
- products
- stock movements

## Notes

- API routes follow the `/api/v1/...` convention.
- Some endpoints are platform-admin only.
- Tenants must be approved before tenant-scoped data operations can succeed.

