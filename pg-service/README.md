# PG Service

Manages building, floors, rooms, and PG setup. Called by auth-service during owner signup.

## Tech
- Java 21, Gradle, Spring Boot 3.3.x
- PostgreSQL

## Required environment variables

| Variable           | Purpose                                         |
|--------------------|-------------------------------------------------|
| `DB_URL`           | JDBC URL (default `jdbc:postgresql://localhost:5432/pg_db`) |
| `DB_USERNAME`      | Postgres user                                   |
| `DB_PASSWORD`      | Postgres password                               |
| `INTERNAL_API_KEY` | Must match the value set in auth-service        |

## Run

```bash
./gradlew bootRun
```

## Notes
- All requests to internal endpoints (e.g. `/api/pg/setup`) must include header `X-Internal-API-Key`.
- Requests with a missing or mismatched key are rejected with `401`.
