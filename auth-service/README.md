# Auth Service

Handles owner signup and signin. Calls PG Service internally during signup.

## Tech
- Java 21, Gradle, Spring Boot 3.3.x
- PostgreSQL
- JWT (JJWT 0.12.x)

## Required environment variables

Before running the app, set these in your shell or IDE run configuration. **Do not commit them.**

| Variable           | Purpose                                          |
|--------------------|--------------------------------------------------|
| `DB_URL`           | JDBC URL (default `jdbc:postgresql://localhost:5432/auth_db`) |
| `DB_USERNAME`      | Postgres user                                    |
| `DB_PASSWORD`      | Postgres password                                |
| `JWT_SECRET`       | Base64-encoded secret, at least 32 bytes         |
| `PG_SERVICE_URL`   | Base URL of pg-service (default localhost:8082)  |
| `INTERNAL_API_KEY` | Shared secret between auth-service and pg-service|

### Generating a JWT secret

```bash
openssl rand -base64 48
```

### Generating an internal API key

```bash
openssl rand -hex 32
```

## Run

```bash
./gradlew bootRun
```

## Notes
- Passwords are stored as BCrypt hashes — never plain text.
- The internal API key must match the one configured in pg-service.
- If pg-service fails during signup, the user row is rolled back (compensating action).
