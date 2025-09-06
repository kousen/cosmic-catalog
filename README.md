# Cosmic Catalog

A Spring Boot application for managing astronomical observations from telescopes like Hubble and JWST. This project demonstrates the power of AI agent collaboration, evolving from v5 to v15 through contributions from multiple AI agents (Gemini CLI, Claude Code, and OpenAI Codex CLI).

## 🚀 AI Agent Collaboration Demo

This repository showcases four methods of AI agent collaboration:
1. **Programmatic Orchestration** - See [OperaGenerator](https://github.com/kousen/OperaGenerator)
2. **Manual Multi-Terminal** - Used to create v5-v15 of this project
3. **MCP Server Wrapping** - See [GeminiMcpServer](https://github.com/kousen/GeminiMcpServer)
4. **Claude Code Agent Orchestration** - Using gemini-analyzer agent

View the presentation slides: `npm run dev` (after `npm install`)

## Tech Stack

*   **Java 21** (virtual threads available, standard features used)
*   **Spring Boot 3.5.5**
*   **Gradle (Groovy DSL)** via the included wrapper
*   **Spring Data JPA** with **H2** (file-backed by default; in-memory in tests)
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

## E2E Tests

The project includes Playwright end-to-end tests that verify the application's UI and API functionality.

To run E2E tests locally:

```bash
cd cosmic-catalog/e2e
npm ci
npm run install:browsers
npm test
```

The E2E tests will:
- Start the Spring Boot application automatically
- Verify the home page loads correctly
- Test the import API endpoint
- Verify the featured observations API
- Check pagination functionality

E2E tests are also run automatically in CI on pull requests and pushes to main.

## API Documentation

- Human-friendly guide: `docs/API.md`
- OpenAPI spec: `docs/openapi.yaml`
- Postman collection: `docs/postman_collection.json`

Swagger UI (auto-loaded from the spec):
- Start the app, then open: `http://localhost:8080/swagger-ui.html` (or `/swagger-ui/index.html`)
- The UI is configured to load `/openapi.yaml` served from static resources

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

### Docker Compose (PostgreSQL persistence)

Bring up Postgres and the app with the `postgres` profile enabled:

```bash
docker compose up --build
```

Details:
- Postgres runs on `localhost:5432` with db/user/password `cosmic`
- App runs on `localhost:8080` with `SPRING_PROFILES_ACTIVE=postgres`
- Data persists in the `db-data` volume
```

## Architecture

See `docs/ARCHITECTURE.md` for layers, decisions, and future enhancements.

## Presentation

This project includes a Slidev presentation about AI agent collaboration:

```bash
# Install dependencies
npm install

# Start the presentation server
npm run dev

# Build static presentation
npm run build
```

The presentation covers:
- Four methods of AI agent collaboration
- Real examples with working code
- Screenshots of the running application
- Links to all related repositories

## Git Tags

The repository includes tags v5 through v15 showing the evolution through AI agent contributions:
- **v5**: Starting point - feature-complete Spring Boot app
- **v6-v7**: Gemini CLI - Added integration tests, version conflict tests
- **v8**: Claude Code - Service extraction, SOLID principles
- **v9-v10**: OpenAI Codex CLI - API documentation, Docker, CI/CD
- **v11-v12**: Claude Code - Advanced services, caching, exceptions
- **v13**: Gemini CLI - Global exception handling
- **v15**: Claude Code - E2E Playwright tests

## Contributing

Pull requests welcome. Please:
- Keep changes focused and include tests where appropriate
- Use clear commit messages and reference tasks/tags in `AI_AGENT_TASKS.md`
