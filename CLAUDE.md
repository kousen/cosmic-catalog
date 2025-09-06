# CLAUDE.md - Project Context for AI Assistants

## Project Overview

**Cosmic Catalog** is a Spring Boot application for managing astronomical observations from telescopes like Hubble and JWST. This project serves as a demonstration of AI agent collaboration, showing how multiple AI tools can work together to evolve a codebase from a basic working application (v5) to a production-ready system (v15).

## Key Technologies

- **Java 21** with modern features (pattern matching, switch expressions, var)
- **Spring Boot 3.5.5** with Spring Data JPA
- **H2 Database** (file-backed, PostgreSQL-ready)
- **Thymeleaf** for server-side rendering
- **Gradle** build system
- **JUnit 5** for testing
- **Playwright** for E2E tests
- **Docker** support with multi-stage builds
- **Swagger/OpenAPI** for API documentation

## Project Structure

```
cosmic-catalog/
├── src/
│   ├── main/
│   │   ├── java/com/example/cosmiccatalog/
│   │   │   ├── controller/     # REST & MVC controllers
│   │   │   ├── service/        # Business logic
│   │   │   ├── model/          # JPA entities
│   │   │   ├── dto/            # Data transfer objects
│   │   │   └── exception/      # Custom exceptions
│   │   └── resources/
│   │       ├── templates/      # Thymeleaf templates
│   │       └── static/          # Static assets
│   └── test/                   # Unit & integration tests
├── e2e/                        # Playwright E2E tests
├── docs/                       # Documentation
├── images/                     # Screenshots for presentation
└── slides.md                   # Slidev presentation
```

## Core Features

1. **Observation Management**
   - Create, read, update observations
   - Scoring system based on exposure time, filters, instruments
   - Deduplication (5 arcsecond threshold)
   - Optimistic locking for concurrent updates

2. **Featured Observations**
   - Top-scored approved observations
   - Caching with Caffeine
   - Special "Don't Panic" badge for score 42

3. **Import Functionality**
   - Sample data import
   - Realistic telescope data import
   - Batch tracking with ImportBatch entity

4. **API Documentation**
   - OpenAPI 3.0 specification
   - Swagger UI at `/swagger-ui.html`
   - Comprehensive REST endpoints

## Important Commands

```bash
# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run linting/formatting (if configured)
./gradlew spotlessApply

# Build Docker image
docker build -t cosmic-catalog .

# Run with Docker Compose (PostgreSQL)
docker compose up --build

# Run E2E tests
cd e2e && npm test

# Start presentation
npm run dev
```

## AI Agent Collaboration History

This project demonstrates four methods of AI agent collaboration:

### Method 1: Programmatic (LangChain4j)
- Example: [OperaGenerator](https://github.com/kousen/OperaGenerator)
- Agents share memory programmatically

### Method 2: Manual Multi-Terminal
- Used to create v5-v15 of this project
- Three terminal windows with different agents
- Manual coordination between agents

### Method 3: MCP Server Wrapping
- Example: [GeminiMcpServer](https://github.com/kousen/GeminiMcpServer)
- Wraps CLI tools as MCP servers
- Reusable across different agents

### Method 4: Claude Code Agent Orchestration
- Uses gemini-analyzer agent
- Token-efficient delegation
- Intelligent task routing

## Version Evolution

- **v5**: Starting point - feature-complete app
- **v6-v7**: Gemini added integration tests
- **v8**: Claude extracted services, applied SOLID
- **v9-v10**: Codex added API docs, Docker, CI/CD
- **v11-v12**: Claude added advanced services
- **v13**: Gemini added exception handling
- **v15**: Claude added E2E tests

## Current State

The application is production-ready with:
- Comprehensive test coverage
- Professional API documentation
- E2E testing with Playwright
- Docker deployment options
- CI/CD pipeline (GitHub Actions)
- Caching and performance optimizations

## Common Tasks for AI Assistants

When working on this project, consider:

1. **Testing**: Ensure all tests pass with `./gradlew test`
2. **Code Style**: Follow existing patterns (Java 21 features, Spring conventions)
3. **Documentation**: Update API docs when adding endpoints
4. **Git Tags**: Reference appropriate version tags when making changes
5. **Presentation**: The `slides.md` file contains a 15-minute presentation about AI collaboration

## Presentation Assets

The project includes a Slidev presentation with:
- `slides.md`: Main presentation file (14 slides)
- `slides-original.md`: Comprehensive 40+ slide version
- `images/`: Screenshots of the running application
  - `cosmic_catalog_display.png`: Main UI
  - `cosmc_catalog_swagger_api.png`: Swagger documentation

## Key Design Decisions

1. **Service Layer Pattern**: Clear separation of concerns
2. **DTO Pattern**: API contracts separate from entities  
3. **Optimistic Locking**: Version-based conflict resolution
4. **Caching Strategy**: Caffeine for featured observations
5. **Test Isolation**: In-memory H2 for tests
6. **Modern Java**: Use of var, pattern matching, records

## Areas for Future Enhancement

- Spring Security integration
- PostgreSQL migration for production
- Monitoring with Micrometer/Actuator
- API versioning strategy
- GraphQL endpoint
- WebSocket for real-time updates

## Important Notes

- The scoring system gives special treatment to score 42 (Hitchhiker's Guide reference)
- Deduplication uses ~5 arcsecond threshold for coordinates
- All datetime handling uses UTC
- The application uses file-based H2 by default (`./data/cosmic`)

## Related Repositories

- [Cosmic Catalog](https://github.com/kousen/cosmic-catalog) - This repository
- [OperaGenerator](https://github.com/kousen/OperaGenerator) - Programmatic orchestration example
- [GeminiMcpServer](https://github.com/kousen/GeminiMcpServer) - MCP wrapper for Gemini CLI

When making changes, always run tests and ensure the application starts correctly. The presentation materials should be kept in sync with any significant architectural changes.