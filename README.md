# Cosmic Catalog

A simple Spring Boot application for curating astronomical observations from telescopes like Hubble and JWST. This project serves as the basis for the "AI Codecon Demo" where multiple AI agents collaborate on a codebase.

## Tech Stack

*   **Java 21**
*   **Spring Boot 3.1.0** (intentionally outdated for demo purposes)
*   **Gradle** (Kotlin DSL)
*   **Spring Data JPA** with **H2** (in-memory database)
*   **Thymeleaf** for the simple UI
*   **JUnit 5** for testing

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

## Running Tests

To run the full test suite, use the `test` Gradle task:

```bash
./gradlew test
```

**Note:** In the initial state of this project, there is an intentional failing test in `DeDupServiceTest`. This is part of the demo where an AI agent is tasked with fixing it.

## API Endpoints

*   `GET /api/observations`: Returns a paginated list of all observations.
*   `GET /api/featured`: (To be implemented) Returns a list of top-rated, approved observations.
*   `POST /api/observations/{id}/approve`: (To be implemented) Approves an observation.
*   `GET /health`: (To be implemented by an agent) Returns health and status information about the application.
