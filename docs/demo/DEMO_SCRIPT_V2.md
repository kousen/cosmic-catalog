# AI Codecon Demo Script (V2): The Code Review & Polish

**Presenter:** Ken Kousen
**Time:** 15 Minutes
**Narrative:** Start with a feature-complete but un-reviewed codebase and use AI agents as senior collaborators to review, harden, test, and polish the work for release.

---

### (0:00 - 1:00) Introduction & New Premise

**Narration:**
> "Welcome. In the next 15 minutes, we're going to explore a more advanced, collaborative workflow between a human developer and a team of AI agents. I've just finished a feature sprint on this 'Cosmic Catalog' application. The code is modern and it works, but is it production-ready?
>
> "My goal now is to use my AI agents as a team of senior developers and QA specialists. Their job isn't to write code from scratch, but to review my work, harden it with new tests, refactor it for clarity, and prepare it for release. Let's see how they do."

**(Show a slide with the agents and empty letter grades for the new tasks)**

---

### (1:00 - 4:00) Gemini CLI: The Senior Code Reviewer

**Narration:**
> "First, I need a code review. I'll ask Gemini to act as a senior developer reviewing my pull request. I'll ask it to analyze all the changes I've committed and give me a high-level assessment: find risks, praise good work, and suggest what's missing."

**Agent:** Gemini CLI (this tool)

**Prompt:**
> `Act as a senior developer reviewing a pull request containing all changes since commit cd3a1f3. Analyze the git diff and log. Identify 3-4 potential risks or missing edge cases, praise 2-3 examples of good practice, and suggest one new test case that should be written.`

**Expected Output:**
*   **Praise:** Mentions of good practices like using Java Records for DTOs, modern switch expressions, and adding an `ObservationService`.
*   **Risks:** Identifies potential issues like "the `/approve` endpoint doesn't have a test for version conflicts" or "the scoring logic could be more resilient to null inputs."
*   **Suggestion:** Explicitly suggests writing an integration test for the `409 Conflict` case on the approval endpoint.

**Verification Step:**
> "This is a fantastic review. It understood the code's intent, praised the modern Java features, but more importantly, it found a critical gap in our testing strategy: we're not testing for optimistic locking conflicts. Let's give Gemini an A for the review."

**(Update scoreboard slide)**

---

### (4:00 - 7:00) Gemini CLI: The Test Engineer

**Narration:**
> "Gemini's review gave us a clear action item. Now, let's switch its role to that of a Test Engineer and have it write that missing test."

**Agent:** Gemini CLI (this tool)

**Prompt:**
> `Based on your previous review, write a new integration test for the approveObservation endpoint that specifically triggers and verifies a 409 Conflict response when the expectedVersion does not match the actual version.`

**Expected Output:**
*   A new method in `FeaturedControllerIT.java` (or a new test class) that:
    1.  Creates an observation.
    2.  Calls the approve endpoint once successfully.
    3.  Calls the same endpoint again with the *original* version number.
    4.  Asserts that the HTTP response is a `409 Conflict`.

**Verification Step:**
> "It's generated the test. Now, the most important part: let's run the entire test suite and make sure our new test passes, and that we haven't broken anything else."

**Command:**
```bash
./gradlew test
```
> "A green build. We've successfully hardened our application by testing a critical edge case. That's an A for Gemini as a test engineer."

**(Update scoreboard slide)**

---

### (7:00 - 9:30) Playwright: The UI/E2E Specialist

**Narration:**
> "Our API is now more robust, but we need to ensure the front-end experience is still correct. Let's bring in our E2E specialist, Playwright, to validate the full UI flow for a successful approval."

**Agent:** Playwright

**Prompt:**
> `Generate a headless Playwright test for Java/JUnit that opens http://localhost:8080, finds an observation with the target "Carina", clicks its 'Approve' button, and asserts that this observation now appears in a 'Featured' section on the page.`

**Expected Output:**
*   A new Java file `UiE2ETest.java` with the Playwright test code.
*   Output from the Gradle test run showing the E2E test passing.

**Verification Step:**
> "And there we have it. An automated, end-to-end test that mimics user behavior, proving our UI and backend are working together correctly. Playwright gets an A."

**(Update scoreboard slide)**

---

### (9:30 - 12:00) Junie in IntelliJ: The Refactoring Expert

**Narration:**
> "The code is well-tested, but is it as clean as it could be? For this, we'll turn to an agent that lives in the IDE. Let's ask Junie to analyze the project and see if it has any suggestions for improving the code's structure."

**(Switch to IntelliJ IDEA)**

**Agent:** Junie

**Action:**
*   Trigger Junie to analyze the project.
*   It might suggest refactorings like extracting the logic in `HealthController` into a dedicated `HealthService`, or further simplifying the `ScoringServiceImpl`.
*   Accept one of the suggestions.
*   Run the tests from within the IDE to show everything is still green.

**Verification Step:**
> "A great suggestion. We've made the code more maintainable without changing its behavior, and the tests confirm it. That's an A- for Junie."

**(Update scoreboard slide)**

---

### (12:00 - 14:00) Gemini CLI: The Release Manager

**Narration:**
> "Okay, our code has been reviewed, hardened with tests, and refactored. It's ready to be released. The final step is to communicate these changes to our users. Let's have Gemini act as a Release Manager."

**Agent:** Gemini CLI (this tool)

**Prompt:**
> `Read the git log since commit cd3a1f3. Summarize the two major commits into a clean, three-bullet-point release note suitable for a project update.`

**Expected Output:**
*   A concise, well-formatted markdown list summarizing the major changes, such as:
    *   "Introduced new endpoints `/health` and `/api/featured` for application monitoring and showcasing top observations."
    *   "Enhanced observation approval with optimistic locking to prevent conflicts."
    *   "Modernized the entire codebase with Java 21 features, including Records for DTOs and Switch Expressions for cleaner logic."

**Verification Step:**
> "Perfect. That's a clear, human-readable summary of a lot of technical work. A solid B+ for communication."

**(Update scoreboard slide)**

---

### (14:00 - 15:00) Wrap-up

**Narration:**
> "So let's recap. We started with a developer's finished work and used a team of AI agents to take it to the next level. They acted as senior reviewers, test engineers, and release managers. They didn't just write code; they improved its quality, robustness, and maintainability.
>
> "This collaborative process, where the human provides the creative spark and the AI provides the critical review and polish, is a powerful glimpse into the future of software development. Thank you."

**(Show final scoreboard slide)**
