# Local Development

## Prerequisites

- Java 21
- Maven 3.9+ or the provided Maven Wrapper
- PostgreSQL 17+

## Environment variables

The application reads configuration from environment variables and `.env` files.

Important variables:
- `SPRING_PROFILES_ACTIVE`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `DB_SCHEMA`
- `SERVER_PORT`
- `SERVER_CONTEXT_PATH`
- `JWT_PRIVATE_KEY_PATH`
- `JWT_PUBLIC_KEY_PATH`
- `JWT_ACCESS_TOKEN_EXPIRATION`

See `.env.example` for the default development values.

## Run with Maven

### macOS / Linux
```bash
./mvnw spring-boot:run
```

### Windows
```bat
mvnw.cmd spring-boot:run
```

## Build the project

```bash
./mvnw clean package
```

## Run the packaged JAR

```bash
java -jar target/MutliTenantSaas-0.0.1-SNAPSHOT.jar
```

## Database setup

The application uses Flyway to create the schema objects automatically on startup.

1. Start PostgreSQL.
2. Point the app to the database using the `DB_*` variables.
3. Run the application.

Flyway will apply:
- shared schema migrations from `db/migration/common`
- tenant schema migrations from `db/migration/tenant`

## First request checklist

- Ensure the database is running.
- Ensure the JWT private/public keys are available under `src/main/resources/certs/`.
- Register a tenant.
- Approve the tenant.
- Log in and use the returned JWT.

