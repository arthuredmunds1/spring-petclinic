# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This project is a sample project for Java Spring developers.  It instantiates a website that manages veterinary services for
the owner of a veterinary clinic.  

## Build and Run Commands

Important: Don't use mvnw, despite documentation.  Use mvnres.  It is an alias for mvn which uses a specific settings file.
**Maven (primary):**
```bash
./mvnres spring-boot:run          # Run the application
./mvnres -B verify                # Build and run all tests
./mvnres test -Dtest=ClassName    # Run a single test class
./mvnres package -P css           # Regenerate CSS from SCSS (Bootstrap)
./mvnres spring-boot:build-image  # Build container image
```

**Gradle (alternative):**
```bash
./gradlew bootRun
./gradlew test
./gradlew test --tests OwnerControllerTests
```

**Database profiles** (default is H2 in-memory):
```bash
./mvnres spring-boot:run -Dspring-boot.run.profiles=mysql
./mvnres spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Architecture

Spring Boot 4.0.3 MVC application with Thymeleaf templates and Spring Data JPA.

**Package structure** under `org.springframework.samples.petclinic`:
- `owner/` — Owner, Pet, Visit domain + OwnerController, VisitController, PetController, JPA repositories
- `vet/` — Vet, Specialty domain + VetController, VetRepository
- `model/` — Base JPA classes: `BaseEntity`, `NamedEntity`, `Person` (extended by domain classes)
- `system/` — `CacheConfiguration`, `WebConfiguration`, `WelcomeController`, `PetClinicRuntimeHints`

**Key patterns:**
- Repositories are Spring Data JPA interfaces extending `JpaRepository` — no implementation classes needed
- Domain models use JPA annotations; base classes in `model/` provide `id` and common fields via `@MappedSuperclass`
- Caching via JCache/Caffeine: the `vets` list is cached (see `CacheConfiguration`); use `@Cacheable("vets")` pattern
- Thymeleaf templates live in `src/main/resources/templates/`; fragments in `templates/fragments/`
- `PetClinicRuntimeHints` registers reflection hints for GraalVM native image support

## Testing

- **Unit tests** use `@WebMvcTest` (e.g., `OwnerControllerTests`) with Mockito for repository mocks
- **Integration tests** use `@SpringBootTest` against H2 (`PetClinicIntegrationTests`), MySQL (`MySqlIntegrationTests` via Testcontainers), or PostgreSQL (`PostgresIntegrationTests` via Docker Compose)
- `PetClinicIntegrationTests` includes a `main()` method for launching the app from an IDE with test config

## Code Style

- **Spring Java Format** is enforced at build time — run `./mvnres spring-javaformat:apply` to auto-format
- **Checkstyle** validates style; config is at `src/checkstyle/nohttp-checkstyle.xml`
- Apache 2.0 license headers are required on all source files
- `.editorconfig` enforces UTF-8, LF line endings, 4-space indent for Java/XML

# Repository Etiquette

## Git Workflow
1. **Branch Naming**: `feature/description`, `fix/description`, `chore/description`
2. **Commit Messages**: Follow conventional commits
    - `feat:` New feature
    - `fix:` Bug fix
    - `docs:` Documentation only
    - `style:` Code style changes
    - `refactor:` Code refactoring
    - `test:` Test changes
    - `chore:` Build process or auxiliary tool changes

## Pull Request Process
1. **Self-Review**: Review your own PR first
2. **Description**: Use PR template, link to issue
3. **Tests**: All tests must pass
4. **Screenshots**: Include for UI changes
5. **Size**: Keep PRs under 400 lines when possible

## Code Review Guidelines
- Be constructive and specific
- Suggest improvements, don't just criticize
- Use "we" instead of "you" in comments
- Approve with "LGTM" (Looks Good To Me)

## Merge Strategy
- **Squash and merge** for feature branches
- **Rebase** for updating feature branches
- **No merge commits** in main branch
