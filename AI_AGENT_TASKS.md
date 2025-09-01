# AI Agent Tasks for Cosmic Catalog Demo

## Overview
This document outlines suggested tasks for each AI agent to perform on the Cosmic Catalog codebase. Each agent should read this document, complete tasks they find valuable, commit their changes, and update the "Completion Status" section.

## Important: Agent Autonomy
**You are not limited to these suggestions!** These are starting points to guide you, but you should:
- Use your judgment to determine what would be most valuable
- Feel free to combine, skip, or replace tasks
- Add your own creative improvements
- Focus on what you do best
- Surprise us with innovative solutions

The goal is to demonstrate each AI agent's unique strengths and approaches to improving production-ready code.

## Current State (v5-demo-ready)
- ✅ Spring Boot application with astronomy observation management
- ✅ Java 21 features (records, var, switch expressions)
- ✅ Health endpoint (`/health`)
- ✅ Featured observations endpoint (`/api/featured`)
- ✅ Integration tests for controllers
- ✅ Slidev presentations ready
- ⚠️ Missing: Optimistic locking test for approval endpoint
- ⚠️ Missing: E2E UI tests
- ⚠️ Missing: HealthService extraction (logic in controller)

## Phase 1: Gemini CLI - Code Review & Test Engineering

### Task 1.1: Code Review (Tag: v6-gemini-review)
**Instructions:** Analyze the codebase and create a code review report.

**Required:**
1. Run `git diff v0-initial HEAD` to see all changes
2. Create `docs/CODE_REVIEW.md` with your findings

**Suggested areas to explore (but not limited to):**
- The optimistic locking in `ObservationController.approveObservation()`
- Test coverage gaps
- Error handling patterns
- Performance considerations
- Security concerns
- Code duplication
- API design consistency

**Feel free to:**
- Suggest architectural improvements
- Identify technical debt
- Propose new features
- Highlight any concerning patterns

### Task 1.2: Write Missing Test (Tag: v7-gemini-test)
**Instructions:** Improve test coverage based on your review.

**Required:**
- Add at least one meaningful test
- Ensure all tests pass

**Suggestions (choose what you think is most valuable):**
- Version conflict test for approval endpoint (409 response)
- Edge cases for scoring (score of exactly 42)
- De-duplication threshold boundary tests
- Concurrent modification tests
- Invalid data handling tests
- Performance tests for large datasets

**You may also:**
- Refactor existing tests for clarity
- Add test utilities or fixtures
- Improve test documentation

## Phase 2: Claude Code - Refactoring & Code Quality

### Task 2.1: Service Layer Improvements (Tag: v8-claude-refactor)
**Instructions:** Improve the service layer architecture.

**Required:**
- Improve separation of concerns
- Ensure all tests pass

**Suggestions (choose based on your analysis):**
- Extract `HealthService` from controller
- Create `ApprovalService` for approval logic
- Add `ImportService` for batch operations
- Implement `ValidationService` for business rules

**You're encouraged to:**
- Apply SOLID principles
- Reduce coupling between components
- Improve testability
- Add appropriate design patterns

### Task 2.2: Code Quality Enhancements (Optional)
**Instructions:** Improve overall code quality.

**Ideas to explore:**
- Custom exceptions with proper error codes
- Input validation with Bean Validation
- Logging improvements with proper levels
- Configuration externalization
- Cache implementation for performance
- Database query optimization
- Transaction boundary improvements
- Add missing `@Transactional` annotations
- Implement audit logging

## Phase 3: OpenAI Codex CLI - Documentation & Developer Experience

### Task 3.1: API Documentation (Tag: v9-codex-docs)
**Instructions:** Create comprehensive developer documentation.

**Required:**
- Document the API in a useful format
- Make it easy for developers to use the API

**Suggestions for documentation:**
- OpenAPI/Swagger specification
- Postman collection
- API.md with examples
- curl command cookbook
- Error code reference
- Rate limiting documentation
- Authentication guide (if applicable)
- Versioning strategy

**Code documentation ideas:**
- JavaDoc for public APIs
- Architecture decision records (ADRs)
- Sequence diagrams for complex flows
- Database schema documentation

### Task 3.2: Developer Experience (Tag: v10-codex-dx)
**Instructions:** Improve the developer onboarding experience.

**Ideas to explore:**
- Enhanced README with quick start
- Docker/docker-compose setup
- Development environment setup script
- Sample data generation
- CI/CD pipeline configuration
- Git hooks for code quality
- Performance benchmarking setup
- Monitoring and observability guides
- Troubleshooting guide
- Contributing guidelines

## Phase 4: Additional AI Agents - Creative Contributions

### Task 4.1: Any Agent - Performance & Security (Tag: v11-performance)
**Instructions:** Improve performance or security.

**Performance ideas:**
- Add database indexes
- Implement query optimization
- Add caching layer (Redis/Caffeine)
- Optimize N+1 query problems
- Add pagination optimization
- Implement lazy loading

**Security improvements:**
- Add input sanitization
- Implement rate limiting
- Add CORS configuration
- SQL injection prevention
- XSS protection
- Authentication/Authorization
- Audit logging
- Secrets management

### Task 4.2: Any Agent - Feature Enhancement (Tag: v12-feature)
**Instructions:** Add a valuable feature.

**Feature ideas:**
- GraphQL API endpoint
- WebSocket for real-time updates
- Batch import from CSV/JSON
- Export functionality
- Search with filters
- Statistics dashboard endpoint
- Notification system
- Image processing for observations
- Machine learning scoring
- Data visualization endpoint

## Completion Status

### Gemini CLI
- [x] Task 1.1: Code Review - Completed (see `docs/CODE_REVIEW.md`)
- [x] Task 1.2: Version Conflict Test - Completed
- **Last Updated:** September 1, 2025
- **Notes:** Stabilized failing tests by adding robust global exception handling (400 for validation/type mismatch, 404 not found, 409 version conflict, 500 fallback) and aligning ImportController error handling so Import tests pass. All tests green (`./gradlew test`). Tagged `v13-gemini-fixes`.

### Claude Code  
- [x] Task 2.1: Extract HealthService - Completed
- [x] Task 2.2: Additional Refactoring - Completed (ErrorResponse DTO)
- **Last Updated:** September 1, 2025
- **Notes:** Extracted HealthService from HealthController following SOLID principles. Added structured ErrorResponse DTO for consistent API error handling. All tests pass.

### OpenAI Codex CLI
- [x] Task 3.1: API Documentation - Completed
- [x] Task 3.2: README Enhancement - Completed
 - **Last Updated:** September 1, 2025
 - **Notes:** Added `docs/API.md`, `docs/openapi.yaml`, `docs/postman_collection.json`, and `docs/curl-examples.sh`. Enhanced `README.md` with Quick Start, Docker usage, and pointers to docs. Added JavaDoc to public controllers. Included `docs/ARCHITECTURE.md`. Added Springdoc Swagger UI configured to load `/openapi.yaml` and a `docker-compose.yml` with a Postgres-backed `postgres` profile for persistence.
 - [x] Task 3.3: CI/CD Pipeline - Completed (basic CI)
   - Added GitHub Actions workflow (`.github/workflows/ci.yml`) to run Gradle tests on PRs and main, and build Docker image.
   - Synced API docs to include `POST /api/import/sample` and current `ErrorResponse` shape.

## Next Planned Steps

- Sync OpenAPI + API.md with new import endpoint (`POST /api/import/sample`) and error responses.
- Add GitHub Actions CI to run tests and build Docker image on PRs/main.
- Optionally enable caching (Caffeine) for featured results with cache eviction on approvals/imports.
- Add an E2E UI smoke test (Playwright) for homepage and basic API flows.

### Playwright/Junie
- [ ] Task 4.1: E2E UI Test - Not started
- **Last Updated:** [Date/Time]
- **Notes:**

## Git Tag Convention

After completing your tasks:
```bash
git add .
git commit -m "Agent: [Your agent name] - [Brief description]"
git tag -a [tag-name] -m "[Detailed description]"
git push origin main --tags
```

## Demo Flow

During the presentation, we'll show progression:
1. `v5-demo-ready` - Starting point
2. `v6-gemini-review` - Code review complete
3. `v7-gemini-test` - Test added
4. `v8-claude-refactor` - Service extracted
5. `v9-codex-docs` - Documentation added
6. `v10-codex-dx` - README and DX updates
7. `v10-codex-swagger` - Swagger UI wired to static spec
8. `v10-codex-compose` - Docker Compose with Postgres
9. `v11-claude-services` - Service consolidation + validation
10. `v12-claude-import` - ImportService and endpoint
11. `v13-gemini-fixes` - Stabilization and error handling
12. `v11-e2e-test` - (Optional) E2E test

## Success Criteria

Each agent should:
1. ✅ Complete assigned tasks
2. ✅ Ensure all tests pass (`./gradlew test`)
3. ✅ Commit with descriptive message
4. ✅ Create appropriate tag
5. ✅ Update this document's Completion Status
6. ✅ Push changes to GitHub

## Notes for Agents

- The seeded bug in `DeDupServiceImpl` (line 35) has been intentionally left at threshold 50 (should be ~5)
- The "Don't Panic" Easter egg triggers when score equals 42
- Use Java 21 features where appropriate
- Keep changes focused and atomic for clear demo progression
- If you encounter issues, document them in the Notes section

---
*Last Updated: September 1, 2025*
*Demo Date: AI Codecon September 2025*
