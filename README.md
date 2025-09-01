# Cosmic Catalog

A simple Spring Boot application for curating astronomical observations from telescopes like Hubble and JWST. This project serves as the basis for the "AI Codecon Demo" where multiple AI agents collaborate on a codebase.

## Tech Stack

*   **Java 21** (virtual threads available, standard features used)
*   **Spring Boot 3.1.0** (intentionally a bit outdated for demo purposes)
*   **Gradle (Groovy DSL)** via the included wrapper
*   **Spring Data JPA** with **H2** (in-memory database)
*   **Thymeleaf** for the simple UI
*   **JUnit 5** for unit and integration tests

## Quick Start

Run locally with Gradle:

```bash
./gradlew bootRun
```

Then visit `http://localhost:8080`.

Try the API:

```bash
curl -s http://localhost:8080/health | jq
curl -s 'http://localhost:8080/api/featured?limit=2' | jq
```

Or use the ready-made examples: `docs/curl-examples.sh`.

## Building the Project

You can build the project using the included Gradle wrapper:

```bash
./gradlew build
```

The home page is served at `/` and resolves `src/main/resources/templates/index.html`.

## Running Tests

To run the full test suite, use the `test` Gradle task:

```bash
./gradlew test
```

Tests include unit and integration coverage. Integration tests boot the app on a random port and use `TestRestTemplate`.

## API Documentation

- Human-friendly guide: `docs/API.md`
- OpenAPI spec: `docs/openapi.yaml`
- Postman collection: `docs/postman_collection.json`

Key endpoints:
- `GET /api/observations`: pageable list of observations (DTO)
- `GET /api/featured?limit=10`: top-N approved observations
- `POST /api/observations/{id}/approve?expectedVersion=0`: approve with optimistic locking
- `GET /health`: version, counts, last import

Notes:
- Observation scores are calculated on save via `ObservationService` using `ScoringService`.
- De-duplication threshold for near-duplicates is ~5 arcseconds in RA/Dec within same telescope + filter + target.

## Configuration

`src/main/resources/application.properties` contains:

```
spring.application.name=cosmic-catalog
app.version=1.0.0
```

Update `app.version` as needed; it is reported by `GET /health`.

## Docker

Build and run using Docker (no local JDK/Gradle required):

```bash
docker build -t cosmic-catalog .
docker run --rm -p 8080:8080 cosmic-catalog
```

Pass JVM options if needed:

```bash
docker run --rm -p 8080:8080 -e JAVA_OPTS="-Xms256m -Xmx512m" cosmic-catalog
```

## Architecture

See `docs/ARCHITECTURE.md` for layers, decisions, and future enhancements.

## Contributing

Pull requests welcome. Please:
- Keep changes focused and include tests where appropriate
- Use clear commit messages and reference tasks/tags in `AI_AGENT_TASKS.md`
