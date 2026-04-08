# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This project is a sample project for Java Spring developers.  It instantiates a website that manages veterinary services for
the owner of a veterinary clinic.  It uses Java, Thymeleaf, and an H2 in-memory database.

## Agent Docs

You can find specific information for tasks in agent_docs.  Use the following:

agent_docs
|---build_and_run_commands.md
|---git_repository_etiquette.md
|---service_architecture.md

## Build and Run Commands

Important: Don't use mvnw, despite documentation.  Use mvnres.  It is an alias for mvn which uses a specific settings file.

Here are the most important commands you need to know.
**Maven (primary):**
```bash
./mvnres spring-boot:run          # Run the application
./mvnres -B verify                # Build and run all tests
```

More information can be found in agent_docs/build_and_run_commands.md

## Architecture

Spring Boot 4.0.3 MVC application with Thymeleaf templates and Spring Data JPA.

More information about architecture can be found in agent_docs/service_architecture.md

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

This describes how you should author git commits.  It tells you how to name branches, author code commits, actions you should
take during a pull request, your conduct during code reviews, and how you should merge code.

More information about this can be found in agent_docs/git_repository_etiquette.md
