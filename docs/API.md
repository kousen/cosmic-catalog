# Cosmic Catalog API

This document describes the public HTTP API exposed by Cosmic Catalog.
All responses are JSON unless stated otherwise.

- Base URL: `http://localhost:8080`
- Versioning: Semver reported via `GET /health` (`app.version`)
- Authentication: None (demo app)
- Errors: Structured `ErrorResponse` objects

## Error Model

```
{
  "error": "VERSION_CONFLICT|NOT_FOUND|VALIDATION_ERROR|BAD_REQUEST|IO_ERROR|INTERNAL_ERROR",
  "message": "Human friendly details"
}
```

## Endpoints

### GET /health
Returns application health and counts.

- 200 OK
```
{
  "version": "1.0.0",
  "counts": { "obs": 12, "targets": 5 },
  "lastImport": "2025-08-31T18:21:05"
}
```

### GET /api/observations
Returns a paginated list of observations as DTOs.

- Query params: Spring Data pagination (`page`, `size`, `sort`)
- 200 OK
```
{
  "content": [ ObservationDTO, ... ],
  "pageable": { ... },
  "totalElements": 42,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

ObservationDTO
```
{
  "id": 1,
  "telescope": "JWST",
  "programId": "P-123",
  "targetName": "NGC 1300",
  "ra": 49.922,
  "dec": -19.411,
  "obsDate": "2025-08-31T12:00:00",
  "instrument": "NIRCam",
  "filters": "F200W",
  "exposureSec": 1200,
  "imageUrl": "https://...",
  "score": 87,
  "status": "APPROVED",
  "hasDontPanicBadge": false
}
```

### GET /api/featured
Returns top-N approved observations sorted by score desc.

- Query params:
  - `limit` (int, default 10, 1..100 recommended)
- 200 OK: `ObservationDTO[]`

Example
```
GET /api/featured?limit=3
[
  { "id": 5, "score": 99, ... },
  { "id": 9, "score": 97, ... },
  { "id": 2, "score": 94, ... }
]
```

### POST /api/observations/{id}/approve
Approves an observation. Supports optimistic locking via `expectedVersion`.

- Path: `id` (long)
- Query params:
  - `expectedVersion` (int, optional). If provided and mismatched, returns 409.
- 200 OK: `ObservationDTO` (approved)
- 404 Not Found: `ErrorResponse`
- 409 Conflict: `ErrorResponse` with `error=VERSION_CONFLICT`

Example (success)
```
curl -X POST \
  "http://localhost:8080/api/observations/1/approve?expectedVersion=0"
```
Response 200:
```
{ "id": 1, "status": "APPROVED", ... }
```

Example (version conflict)
```
curl -X POST \
  "http://localhost:8080/api/observations/1/approve?expectedVersion=999"
```
Response 409:
```
{
  "error": "VERSION_CONFLICT",
  "message": "Version conflict: expected 999, but was 0"
}
```

### POST /api/import/sample
Imports sample JWST observations from the bundled JSON file.

- 200 OK: `ImportSummary`
- 500 Internal Server Error: `ErrorResponse` when file read fails

ImportSummary
```
{
  "source": "data/jwst_sample.json",
  "startedAt": "2025-08-31T18:21:00",
  "completedAt": "2025-08-31T18:21:05",
  "totalProcessed": 100,
  "duplicatesFound": 5,
  "imported": 95,
  "status": "SUCCEEDED",
  "notes": "Imported 95 records, skipped 5 duplicates"
}
```

## Curl Cookbook

- List observations (first page):
  - `curl -s "http://localhost:8080/api/observations?size=5" | jq` 
- Featured top 2:
  - `curl -s "http://localhost:8080/api/featured?limit=2" | jq`
- Approve with optimistic locking:
  - `curl -s -X POST "http://localhost:8080/api/observations/1/approve?expectedVersion=0" | jq`
- Health check:
  - `curl -s http://localhost:8080/health | jq`
- Import sample data:
  - `curl -s -X POST http://localhost:8080/api/import/sample | jq`

## Notes

- Scores are computed on save; a score of 42 sets `hasDontPanicBadge=true`.
- Pagination follows Spring Data conventions.
- H2 in-memory DB is used; data resets on restart unless externalized.
