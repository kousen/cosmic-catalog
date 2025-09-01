# AI Codecon Demo Plan — Cosmic Catalog (Java + Spring Boot + Agents)

A fast, coherent 15‑minute demo that shows multiple agent tools collaborating on the same codebase. Theme: “B‑students with infinite office hours.” The app is a small astronomy‑flavored service that curates Hubble/JWST observations with real business rules (import, de‑duplication, scoring, approval) and a tiny UI.

## Message
- B‑students with unlimited resources become an A‑team with a supervising adult.
- Tools are competent but need constraints, tests, and a clear goal.
- One repo, many agents: each agent contributes a focused, verifiable win.

## Run-of-Show (15 minutes)
- 0:00–1:00 — Set the premise and show the grade scoreboard.
- 1:00–4:00 — **Claude + context7: The Specialist.** Audits dependencies, suggests safe version bumps, runs tests, and fixes one specific, targeted bug in `DeDupServiceTest`.
- 4:00–7:30 — **Gemini CLI (Part 1): The Feature Developer.** Implements the new `GET /health` endpoint from scratch, including a new integration test. Runs the full test suite to ensure all changes work together.
- 7:30–10:00 — **Playwright: The QA Agent.** Generates and runs the end-to-end UI test in Java/JUnit, validating the application from the user's perspective.
- 10:00–12:30 — **Gemini CLI (Part 2): The Reviewer/Architect.** Analyzes the entire session's work (git diff, test reports) to provide a high-level summary: top risks, missing tests, and release notes.
- 12:30–14:00 — **Junie in IntelliJ: The Finisher.** Performs a quick, safe refactoring within the IDE to improve code quality.
- 14:00–15:00 — Wrap with grades, 1 insight per tool, and “Don’t Panic” callback.

## Tech Stack
- Java 21, Spring Boot (Web, Data JPA), H2 (demo) or Postgres (optional), JUnit 5.
- Gradle Kotlin DSL; version catalog `gradle/libs.versions.toml` with slightly outdated entries (for context7 upgrade moment).
- Optional: Lombok; Flyway/Liquibase (lightweight).
- Playwright for Java (JUnit) for E2E so everything runs via Gradle.

## Domain and Business Logic
- Entities
  - Observation: telescope (HST/JWST), programId, target, ra, dec, obsDate, instrument, filters, exposureSec, imageUrl, score, status (PENDING/APPROVED/REJECTED), `@Version` for optimistic locking.
  - Target: name, ra, dec; one‑to‑many observations.
  - ImportBatch: source, startedAt, completedAt, totals, duplicates, status (SUCCEEDED/FAILED), notes.
  - Tag (optional): many‑to‑many with Observation.
  - Review (optional): reviewer, comment, createdAt.
- Business rules
  - Transactional import: batch is atomic. If invalid rows > threshold, roll back and mark FAILED with reasons.
  - De‑duplication: within a batch, duplicates if same telescope and within X arcseconds and same primary filter; keep higher exposureSec; log a Duplicate event.
  - Scoring: weighted [0..100] from exposureSec, recency, instrument weight, filter boost. Easter egg: if score rounds to 42 → add “Don’t Panic” badge.
  - Approval: curator sets APPROVED; only approved appear in `/featured`. Use optimistic locking; conflicting approvals return 409.

## Endpoints and UI
- POST `/api/imports/run` → import a local snapshot (JSON/CSV under `data/`); returns ImportSummary.
- GET `/api/observations` → pageable search (telescope/target/minScore/status).
- POST `/api/observations/{id}/approve` → approve with optimistic lock.
- GET `/api/featured` → top N approved by score.
- GET `/health` → `{ version, counts, lastImport }`.
- UI (Thymeleaf): search bar + table with Approve/Reject; small “Featured” gallery; “Don’t Panic” badge for score 42.

## Data (offline, no network required)
- `data/hst_sample.json`, `data/jwst_sample.json` with fields: obsId, programId, target, ra, dec, obsDate, instrument, filters, exposureSec, imageUrl.
- Include 3–5 intentional near‑duplicates to exercise de‑duplication and scoring.
- Cached docs:
  - `docs/actuator_health_excerpt.md` (health JSON shape reference).
  - `docs/scoring_notes.md` (explain weights and rule of 42).

## Tests (seeded to guide agents)
- Unit
  - `DeDupServiceTest` — one failing case (arcsecond threshold off by ×10).
  - `ScoringServiceTest` — boundary cases and the 42 badge.
- Integration
  - `ImportServiceIT` — invalid ratio triggers rollback.
  - `ObservationControllerIT` — `GET /featured` filters by APPROVED only.
- E2E
  - `UiE2ETest` (Playwright, JUnit) — open `/`, search “Carina”, approve first result, assert it appears in Featured; saves screenshot.

## Agents and Roles
- **Claude Code + context7 (The Specialist)**
  - Use context7 to propose latest stable versions for Spring Boot plugin, `starter-web`, Jackson, JUnit. Include breaking change/CVE notes.
  - Produce a minimal Gradle patch (version catalog and/or plugin block). Run `./gradlew test`.
  - Fix exactly one logic bug (e.g., de‑dup arcsecond threshold) with a ≤10‑line patch.
  - Sub‑agent “Reviewer” returns: `Grade: <letter>`, `Suggestion: <one‑liner>`.
- **Gemini CLI (The Feature Developer & Reviewer/Architect)**
  - **Part 1 (Developer):** Implement a small feature: `GET /health` returning `{ "version": "1.0.0", "counts": {obs:int, targets:int}, "lastImport": iso }`. Add/update JUnit tests. Run `./gradlew test`.
  - **Part 2 (Reviewer):** Consume combined context: git diff of all session changes + full JUnit report.
  - Output: top 5 risks, missing edge-case tests, and a 3‑line release note; end with a letter grade.
- **Playwright (The QA Agent)**
  - Generate the UI test from page description; run under Gradle; save `build/reports/playwright.png`.
- **Junie (The Finisher)**
  - Analyze project; propose 2 refactors (rename variable; extract `HealthInfo` record; move logic from controller to service). Apply one; run tests green.

## Cross‑Agent Moments
- Claude (with context7) authors the safe upgrade and fixes de‑dup → Reviewer grades.
- **Gemini (as Developer)** adds `/health` logic + tests → run Gradle.
- Playwright validates the UI flow end‑to‑end → screenshot artifact.
- **Gemini (as Reviewer)** reviews the whole diff and test report → grades and risk list.
- Junie finishes with a safe refactor in IDEA.

## Short, Copy‑Ready Prompts
- **Claude Code**
  - Audit Gradle deps using context7 for latest stable versions (Spring Boot plugin, starter‑web, jackson‑databind, junit‑jupiter). Summarize breaking changes/CVEs. Produce the minimal version bump patch. Run Gradle tests. If only the de‑dup threshold is wrong, fix it in ≤10 lines and show unified diff. Create a sub‑agent Reviewer that outputs: `Grade: X`, `Suggestion: Y`.
- **Gemini CLI (Part 1 - Developer)**
  - Implement `GET /health` returning `{ "version": "1.0.0", "counts": {obs:int, targets:int}, "lastImport": iso }`. Add a new JUnit integration test for this endpoint. Keep the patch minimal across ≤3 files. Run `./gradlew test`.
- **Gemini CLI (Part 2 - Reviewer)**
  - Given the attached git diff from all previous steps and the full Gradle JUnit report, list the top 5 risks, identify 2-3 missing edge‑case tests (e.g., RA/Dec bounds, dupe tie‑breakers), and write a 3‑line release note. Be concise and end with a letter grade.
- **Playwright (Java, JUnit)**
  - Generate a headless Playwright test that opens `http://localhost:8080`, searches “Carina”, clicks Approve on first row, and asserts the item appears under Featured. 3s timeout; save a screenshot to `build/reports/playwright.png`.
- **Junie**
  - Analyze project, propose 2 refactors; apply the safer one; re‑run all tests.

## Humor Beats
- “Context7 is the kid with the newest textbook.”
- “Claude: B+ — responsible upgrade, tiny nit left.”
- “Gemini (as Coder): B — shipped the feature; tests are solid.”
- “Playwright: the hall monitor — checks what users actually see.”
- “Gemini (as Reviewer): B+ — read everything and nagged about edge cases.”
- “Junie: A‑ — tidied the project like a great TA.”
- Badge: “Score 42 → Don’t Panic.”

## Prep Checklist
- Cache context7 and Firecrawl/Tavily outputs under `snapshots/` and `docs/`.
- Pre‑warm app on `:8080`. Keep E2E test `@Disabled` until live.
- Terminals pre‑typed; IDEA project open to service classes.
- Tags: `v0-before`, `v1-context7-upgrade`, `v2-claude-dedup-fix`, `v3-codex-feature`, `v4-playwright-ui`.
- Fallbacks: 15–30s clips for each step; a static screenshot of Featured page.

## Risks and Mitigations
- Network hiccups → use cached snapshots; show that they came from MCP.
- Long test runs → prefer unit/integration scope; limit data set.
- Agent over‑editing → constrain prompts (≤lines, ≤files); keep patches tiny.
- Port conflicts → pre‑start once; if needed, use `server.port=8081` fallback.

## Optional Variants (if time allows)
- Postgres profile with Testcontainers for a quick “prod‑like” spin.
- Cursor cameo: open the repo and ask it to add one more missing test.
- Swap Codex task to implement optimistic‑locking approval with a 409 test.

