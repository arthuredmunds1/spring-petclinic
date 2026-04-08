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
