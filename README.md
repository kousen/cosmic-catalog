# Cosmic Catalog

A simple Spring Boot application for curating astronomical observations from telescopes like Hubble and JWST. This project serves as the basis for the "AI Codecon Demo" where multiple AI agents collaborate on a codebase.

## Tech Stack

*   **Java 21** (virtual threads available, standard features used)
*   **Spring Boot 3.1.0** (intentionally a bit outdated for demo purposes)
*   **Gradle (Groovy DSL)** via the included wrapper
*   **Spring Data JPA** with **H2** (in-memory database)
*   **Thymeleaf** for the simple UI
*   **JUnit 5** for unit and integration tests

## Building the Project

You can build the project using the included Gradle wrapper:

```bash
./gradlew build
```

## Running the Application

To run the application, use the `bootRun` Gradle task:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.

The home page is served at `/` and resolves `src/main/resources/templates/index.html`.

## Running Tests

To run the full test suite, use the `test` Gradle task:

```bash
./gradlew test
```

Tests include unit and integration coverage. Integration tests boot the app on a random port and use `TestRestTemplate`.

## API Endpoints

*   `GET /api/observations`: Pageable list of observations (DTO).
*   `GET /api/featured`: Top-N approved observations, sorted by score desc. Query param: `limit` (default 10).
*   `POST /api/observations/{id}/approve`: Approves an observation; supports optimistic locking via `expectedVersion` query param, returns 409 on version conflict.
*   `GET /health`: Returns `{ version, counts: { obs, targets }, lastImport }`. Version comes from `app.version` in `application.properties`.

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
