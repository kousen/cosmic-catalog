# Architecture Overview

Cosmic Catalog is a small Spring Boot 3 application with a clean layered architecture:

- Web (Controller): `HealthController`, `ObservationController`, `FeaturedController`
- Service: `HealthService`, `ObservationService`, `ScoringService(Impl)`, `DeDupService(Impl)`
- Data: JPA Entities (`Observation`, `Target`, `ImportBatch`) and Repositories
- DTOs: `ObservationDTO`, `HealthInfo`, `ErrorResponse`

## Key Decisions

- Separation of Concerns: Health logic moved from controller into `HealthService` for testability and SRP.
- DTOs as Records: Public API returns `ObservationDTO` Java records for immutable, concise payloads.
- Optimistic Locking: `Observation.version` with explicit `expectedVersion` guard in approval endpoint.
- Error Consistency: `ErrorResponse` standardizes client-facing errors across endpoints.
- Stateless API: Demo intentionally omits auth; easy to add Spring Security later.

## Request Flow

HTTP -> Controller -> Service (business rules, scoring, dedup) -> Repository -> Entity

## Data Model Highlights

- `Observation`: telescope metadata, coordinates, `score`, `status`, `version` for optimistic locking.
- `Target`: named celestial object; repository supports counts for health.
- `ImportBatch`: tracks bulk imports; latest completion time shown in `/health`.

## Testing

- Integration tests boot app on random port and exercise REST endpoints via `TestRestTemplate`.
- Optimistic locking behavior tested via expected 409 responses.

## Future Enhancements

- Add Springdoc OpenAPI UI to serve `docs/openapi.yaml` (or generate at runtime).
- Externalize DB (PostgreSQL) and add Docker Compose profile.
- Add caching layer for featured queries if necessary.

