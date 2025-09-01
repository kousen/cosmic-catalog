# How to Demonstrate the Cosmic Catalog Project

A structured guide for presenting the AI agent collaboration demo at AI Codecon September 2025.

## 1. Opening: The Challenge (2 minutes)

Start with the problem:
- "How can multiple AI agents collaborate on a real codebase?"
- "Each AI tool has different strengths - how do we leverage them?"
- Show the initial state at tag `v5-demo-ready`

```bash
git checkout v5-demo-ready
./gradlew bootRun
```

Show the basic app: simple Spring Boot with observations, no tests for optimistic locking, basic structure.

## 2. The AI Agent Journey (10-15 minutes)

Walk through the progression using git tags to show each agent's contribution:

### Gemini's Contribution - Code Review & Testing
```bash
git checkout v7-gemini-test
git diff v5-demo-ready v7-gemini-test --stat
```
- Show `docs/CODE_REVIEW.md` - Gemini identified issues
- Show the version conflict test it added
- "Gemini excels at analysis and test coverage"

### Claude's Contribution - Refactoring & Services
```bash
git checkout v12-claude-import
git diff v7-gemini-test v12-claude-import --stat
```
- Show extracted `HealthService`, `ApprovalService`
- Show custom exceptions and global error handler
- Show import functionality
- "Claude focuses on clean architecture and SOLID principles"

### Codex's Contribution - Documentation & DevEx
```bash
git checkout v10-codex-dx
```
- Show API documentation (`docs/API.md`, `docs/openapi.yaml`)
- Show Docker setup and docker-compose
- Demo: `docker-compose up` for Postgres persistence
- "Codex enhances developer experience"

### Claude's E2E Tests - Quality Assurance
```bash
git checkout v15-claude-e2e
cd e2e && npm test
```
- Show Playwright tests running
- Show GitHub Actions CI pipeline
- "Comprehensive E2E testing ensures quality"

## 3. Live Demo: The Final Product (5 minutes)

```bash
git checkout main
./gradlew bootRun
```

**Terminal 1:** Show the app running

**Terminal 2:** Run the curl examples
```bash
./docs/curl-examples.sh
```

**Terminal 3:** Show the E2E tests
```bash
cd e2e && npm test
```

**Browser:** 
- Open `http://localhost:8080` - show the UI
- Open `http://localhost:8080/swagger-ui.html` - show API docs
- Open GitHub Actions tab - show green CI builds

## 4. Key Demonstration Points

### Show the Collaboration Flow:
```bash
# Show the git history with tags
git log --oneline --graph --decorate

# Show the progression
gitk --all  # or use your favorite git GUI
```

### Highlight the AI_AGENT_TASKS.md:
```bash
cat AI_AGENT_TASKS.md | grep -A 3 "Completion Status"
```
Show how each agent updated their section

### Show the Feature Matrix:
Open `ai_agent_comparison.md` in presentation mode:
```bash
npx @slidev/cli ai_agent_comparison.md
```

## 5. The "Wow" Moments (2 minutes)

### Version Conflict Demo:
```bash
# Terminal 1: Get an observation
curl -s http://localhost:8080/api/observations?size=1 | jq '.'

# Terminal 2: Try to approve with wrong version
curl -X POST "http://localhost:8080/api/observations/1/approve?expectedVersion=999"
# Shows 409 Conflict with proper error message
```

### Import and Featured Demo:
```bash
# Import sample data
curl -X POST http://localhost:8080/api/import/sample | jq '.'

# Get featured observations (cached!)
time curl http://localhost:8080/api/featured?limit=3 | jq '.'
time curl http://localhost:8080/api/featured?limit=3 | jq '.'  # Much faster!
```

### The Don't Panic Easter Egg:
Show an observation with score 42 getting the special badge

## 6. Talking Points During Demo

- **On Gemini:** "Notice how Gemini found the missing test coverage and added comprehensive integration tests"
- **On Claude:** "See how Claude extracted services and added proper error handling - this is production-ready code"
- **On Codex:** "Look at the complete documentation - OpenAPI spec, Postman collection, curl examples"
- **On CI/CD:** "Every commit triggers tests, including E2E tests with Playwright"

## 7. Conclusion: Key Takeaways (2 minutes)

Show the final stats:
```bash
# Count lines of code added
git diff v5-demo-ready main --stat

# Show test coverage
./gradlew test
```

**Key Messages:**
1. "Different AI agents have different strengths"
2. "They can work together on the same codebase"
3. "The result is production-ready, well-tested, documented code"
4. "AI agents are tools - human oversight guides the strategy"

## 8. Quick Reference Commands

Keep these handy during the demo:

```bash
# Start fresh
git checkout v5-demo-ready && ./gradlew clean

# Run the app
./gradlew bootRun

# Run tests
./gradlew test

# See changes between versions
git diff v5-demo-ready v15-claude-e2e --stat

# Docker compose
docker-compose up

# E2E tests
cd e2e && npm test

# API tests
./docs/curl-examples.sh
```

## 9. Backup Plans

If something fails:
- Have the GitHub repo open to show the code
- Have screenshots of successful CI runs
- Pre-record a video of the E2E tests running
- Have the git history visualization ready as an image

## 10. The Story Arc

Tell it as a story:
1. **Act 1:** "We started with a basic Spring Boot app"
2. **Act 2:** "Three AI agents collaborated to improve it"
3. **Act 3:** "We ended with production-ready, tested, documented code"

## Key Demo Checkpoints

- [ ] Java 21 installed and working
- [ ] Docker running (for docker-compose demo)
- [ ] Node.js 20+ installed (for E2E tests)
- [ ] Playwright browsers installed (`cd e2e && npm install && npm run install:browsers`)
- [ ] PostgreSQL not running on port 5432 (conflicts with docker-compose)
- [ ] Port 8080 free
- [ ] Git tags all present (`git tag -l`)
- [ ] Internet connection (for npm install if needed)

## Timing Guide

- **2 min:** Introduction and problem statement
- **3 min:** Gemini's contributions
- **3 min:** Claude's refactoring
- **3 min:** Codex's documentation
- **2 min:** Claude's E2E tests
- **5 min:** Live demo of final product
- **2 min:** Wow moments and Easter eggs
- **2 min:** Conclusion and takeaways
- **3 min:** Q&A buffer

**Total: ~25 minutes**

## Remember

The magic isn't just what each agent did, but how they worked together, building on each other's contributions to create something better than any single agent could have done alone.

## Emergency Commands

If the app won't start:
```bash
./gradlew clean build
lsof -i :8080  # Check what's using port 8080
kill -9 <PID>  # Kill process using the port
```

If tests fail:
```bash
./gradlew clean test --info
```

If Docker issues:
```bash
docker-compose down -v  # Clean up volumes
docker system prune -a  # Nuclear option: clean everything
```

## Final Checklist

Before going on stage:
- [ ] Checkout main branch
- [ ] Run `./gradlew clean build`
- [ ] Test that app starts
- [ ] Test that E2E tests pass
- [ ] Have backup slides ready
- [ ] Have GitHub repo open in browser
- [ ] Have terminals ready with commands pre-typed
- [ ] Disable notifications
- [ ] Increase terminal font size
- [ ] Have water ready

Good luck with your presentation!