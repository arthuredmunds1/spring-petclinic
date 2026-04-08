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
