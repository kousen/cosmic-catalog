# AI Codecon Demo Script: Cosmic Catalog

**Presenter:** Ken Kousen
**Time:** 15 Minutes

---

### (0:00 - 1:00) Introduction & Premise

**Narration:**
> "Welcome. In the next 15 minutes, we're going to see a team of AI agents collaborate to build and refine a real-world Java application. I want you to think of these agents not as flawless experts, but as a team of B-students with infinite office hours. They're competent, they're fast, but they need clear goals and adult supervision.

> "Our project is the 'Cosmic Catalog', a small Spring Boot service to curate astronomical observations. Our team of agents will have to deal with outdated dependencies, fix a logic bug, add a new feature, and run tests to prove their work. I'll be the supervisor, and we'll keep score as we go."

**(Show a slide with the agents and empty letter grades)**

---

### (1:00 - 4:00) Claude + context7: The Specialist

**Narration:**
> "First up is Claude, paired with a specialist tool called context7. Its job is to audit our dependencies and fix a bug. The project is using an older version of Spring Boot. We need to upgrade it, but safely.

> "I'll give it a prompt asking it to find the latest stable versions, summarize any breaking changes, and produce a patch. Then, it needs to run the tests. It should find a single failing test and fix it."

**Agent:** Claude Code + context7

**Prompt:**
> `Audit Gradle deps using context7 for latest stable versions (Spring Boot plugin, jackson-databind). Summarize breaking changes/CVEs. Produce the minimal version bump patch. Then, run the Gradle tests. If only the de-dup threshold test is failing, fix the logic bug in DeDupServiceImpl in ≤10 lines and show the unified diff. Create a sub-agent Reviewer that outputs: Grade: X, Suggestion: Y.`

**Expected Output:**
*   A summary of new versions and potential breaking changes.
*   A patch for the `build.gradle` file.
*   The output of the test run, showing the `DeDupServiceTest` failure.
*   A small diff fixing the arcsecond threshold bug in `DeDupServiceImpl`.
*   A final grade from its "Reviewer" sub-agent, like `Grade: B+`, `Suggestion: Upgrade was successful, but the test suite should be more comprehensive.`

**Verification Step:**
> "Claude has upgraded the dependencies and claims to have fixed the bug. Let's verify that by running the tests ourselves."

**Command:**
```bash
./gradlew test
```
> "And we have a green build. Great. Let's give Claude a B+."

**(Update scoreboard slide)**

---

### (4:00 - 7:30) Gemini CLI (Part 1): The Feature Developer

**Narration:**
> "Next, it's Gemini's turn. I'm going to ask it to act as our feature developer. The task is to add a new `/health` endpoint to our application, which is a standard practice for microservices. It also needs to add a new integration test for this endpoint."

**Agent:** Gemini CLI (this tool)

**Prompt:**
> `Implement a GET /health endpoint that returns a JSON object with "version", "observationCount", and "lastImportTimestamp". Add a new JUnit integration test for this endpoint. Keep the patch minimal across all files. Then, run ./gradlew test to confirm.`

**Expected Output:**
*   Modifications to `ObservationController.java` to add the `/health` endpoint.
*   A new test file, `HealthEndpointIT.java`, with a test case for the new endpoint.
*   The output of the successful `./gradlew test` run.

**Verification Step:**
> "Gemini has added the code and the test. The build is green. Let's give it a B for shipping the feature."

**(Update scoreboard slide)**

---

### (7:30 - 10:00) Playwright: The QA Agent

**Narration:**
> "Our application has a simple web UI. We need to make sure it works from a user's perspective. For that, we'll use Playwright to generate an end-to-end test. This agent will act as our QA specialist."

**Agent:** Playwright

**Prompt:**
> `Generate a headless Playwright test for Java/JUnit that opens http://localhost:8080, searches for “Carina”, clicks the first 'Approve' button, and asserts that the item appears in the 'Featured' section. Save a screenshot to build/reports/playwright.png.`

**Expected Output:**
*   A new Java file `UiE2ETest.java` containing the Playwright test code.
*   Output from the Gradle test run showing the E2E test passing.
*   A screenshot file saved to the specified path.

**Verification Step:**
> "The test was generated and it passed. We now have an end-to-end test validating our UI flow. Playwright gets an A for being our diligent hall monitor."

**(Update scoreboard slide)**

---

### (10:00 - 12:30) Gemini CLI (Part 2): The Reviewer/Architect

**Narration:**
> "Now for my favorite part. I'm going to ask Gemini to switch hats from a developer to a code reviewer or architect. I'll feed it the `git diff` of all the changes made so far, along with the test reports, and ask for a high-level analysis."

**Agent:** Gemini CLI (this tool)

**Prompt:**
> `Given the attached git diff from all previous steps and the full Gradle JUnit report, list the top 5 risks, identify 2-3 missing edge-case tests, and write a 3-line release note. Be concise and end with a letter grade.`

**Expected Output:**
*   A list of risks (e.g., "The health endpoint is not transactional and could show inconsistent counts.", "The UI has no error handling for failed approvals.").
*   A list of missing tests (e.g., "No test for tie-breaking duplicate observations.", "No test for handling empty import files.").
*   A short, well-formatted release note.
*   A final letter grade for the overall session.

**Verification Step:**
> "This is incredibly valuable. It didn't just look at the code, it analyzed the *changes* and the test results to give us a strategic overview. That's a solid B+ for thinking like a lead developer."

**(Update scoreboard slide)**

---

### (12:30 - 14:00) Junie in IntelliJ: The Finisher

**Narration:**
> "Finally, let's switch to the IDE. We'll use an agent called Junie to do a quick refactoring. These agents are great for cleaning up code and improving maintainability."

**(Switch to IntelliJ IDEA)**

**Agent:** Junie

**Action:**
*   Trigger Junie to analyze the project.
*   It should propose a few refactorings (e.g., extract a `HealthInfo` record, move logic from controller to service).
*   Accept one of the safer refactorings.
*   Run the tests one last time from within the IDE to show they are still green.

**Verification Step:**
> "A quick, safe refactoring, and all tests still pass. Junie gets an A- for being a great TA and tidying up our work."

**(Update scoreboard slide)**

---

### (14:00 - 15:00) Wrap-up

**Narration:**
> "So, in just under 15 minutes, our team of B-students has performed a dependency upgrade, fixed a bug, added a feature with tests, created an E2E test, reviewed the entire project for risks, and refactored the code. No single agent was perfect, but by combining their strengths and providing clear direction, we achieved a great result.

> "The key takeaway is this: Don't Panic. These tools are here to help, and with the right supervision, they can turn a team of B-students into an A-team. Thank you."

**(Show final scoreboard slide with all grades)**
