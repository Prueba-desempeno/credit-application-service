# Credit Application Service

Summary
This repository contains a Java Spring Boot application (`credit-app`), a third-party mock (`risk-central-mock`), and PostgreSQL database configuration. Project managed with Maven and designed to run locally or with Docker / Docker Compose.

Table of Contents
- [Project structure](#project-structure)
- [Requirements](#requirements)
- [Main environment variables](#main-environment-variables)
- [Build and local execution (without Docker)](#build-and-local-execution-without-docker)
- [Execution with Docker / Docker Compose](#execution-with-docker--docker-compose)
- [Dockerfile - best practices](#dockerfile---best-practices)
- [Postman Collection - quick usage](#postman-collection---quick-usage)
- [Curl: practical examples](#curl-practical-examples)
- [Verification and diagnostics](#verification-and-diagnostics)
- [Common problems and solutions](#common-problems-and-solutions)
- [Useful commands summary](#useful-commands-summary)
- [Security and production notes](#security-and-production-notes)

## Project structure
- `pom.xml` — Maven configuration (dependencies, plugins).
- `Dockerfile` — multistage image for production.
- `docker-compose.yml` — orchestrates services: `credit-app`, `risk-central-mock`, `db`.
- `src/main/java/...` — source code (controllers, services, repositories, security).
- `src/main/resources/` — `application.yml` / `application-*.yml` and other resources.
- `postman_collection_credit_app.json` — collection to import into Postman.
- `README_DOCKER.md` — complementary Docker documentation.

## Requirements
- Java 17 (JDK) to run locally.
- Maven 3.6+ (or use included `./mvnw`).
- Docker Engine and Docker Compose (v2+) for containerized environment.
- Curl or Postman for API testing.

## Main environment variables
Normally defined in `docker-compose.yml` or in `.env`:
- `SPRING_DATASOURCE_URL` (e.g.: `jdbc:postgresql://db:5432/creditdb`)
- `SPRING_DATASOURCE_USERNAME` (e.g.: `postgres`)
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT` (default 8080)
- `JWT_SECRET` (do not version in public repositories)
- `SPRING_PROFILES_ACTIVE` (optional)

Review `src/main/resources/application*.yml` and `docker-compose.yml` to see additional variables used by the app.

## Build and local execution (without Docker)
1. Compile the project:
   mvn -B -DskipTests package

   or with wrapper:
   ./mvnw -B -DskipTests package

2. Run the resulting JAR:
   java -jar target/<jar-name>.jar

   Replace `<jar-name>.jar` with the generated artifact (e.g. `credit-app-0.0.1-SNAPSHOT.jar`) or use `target/*.jar`.

## Execution with Docker / Docker Compose
1. Start and build (foreground mode):
   docker compose up --build

   If your CLI uses the hyphen:
   docker-compose up --build

2. Start in background (detached):
   docker compose up -d --build

3. Stop and remove containers and data volumes:
   docker compose down -v

4. Rebuild without cache (useful for debugging):
   docker compose build --no-cache --progress=plain

## Dockerfile - best practices
- Use multistage: builder (Maven) + runtime (JRE).
- Copy `mvnw`, `.mvn` and `pom.xml` before the rest to leverage cache.
- Copy `target/*.jar` to the final image as `app.jar` to avoid depending on the exact name.

Example of flow in the Dockerfile (indented summary):
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
COPY src src
RUN if [ -x "./mvnw" ]; then ./mvnw -B -DskipTests package; else mvn -B -DskipTests package; fi

    FROM eclipse-temurin:17-jre-alpine
    WORKDIR /app
    COPY --from=builder /workspace/target/*.jar app.jar
    EXPOSE 8080
    ENTRYPOINT ["java","-jar","/app/app.jar"]

## Postman Collection - quick usage
- Import `postman_collection_credit_app.json` into Postman.
- Create an Environment with:
    - `base_url` = `http://localhost:<PORT>` (e.g. `http://localhost:8080`)
    - `access_token` (empty initially)
- Recommended flow:
    1. `Register User` (if applicable)
    2. `Login User` — test script saves `access_token` in the environment variable
    3. Execute protected requests by adding header:
       Authorization: Bearer {{access_token}}

The included collection has example requests for CRUD endpoints and to separate access by role (USER / ADMIN). Adjust bodies and paths if they differ from the actual code.

## Curl: practical examples
- Health:
  curl -v {{base_url}}/actuator/health

- Login and manually extract token (example):
  curl -s -X POST {{base_url}}/auth/login -H "Content-Type: application/json" -d '{"username":"user1","password":"pass"}' | jq -r '.token'

- Authenticated request example:
  curl -v -H "Authorization: Bearer <TOKEN>" -H "Content-Type: application/json" -X GET {{base_url}}/api/credits

(Replace `{{base_url}}` and `<TOKEN>` according to environment.)

## Verification and diagnostics
- View containers and ports:
  docker compose ps

- View logs in real time:
  docker compose logs -f --tail=200

- Logs of a specific service (e.g.: credit-app):
  docker compose logs -f credit-app --tail=200

- Get mapped port of a container:
  docker port <container_name> 8080

- Check Postgres inside the container:
  docker compose exec db pg_isready -U <username>

- Search for the line `Started ... in ... seconds` in logs to confirm successful Spring Boot startup.

## Common problems and solutions
1. Maven detects multiple `main` classes (error: "Unable to find a single main class ...")
    - Cause: several classes with `@SpringBootApplication` in the same artifact.
    - Solutions:
        - Explicitly define `mainClass` in `pom.xml` for `spring-boot-maven-plugin`.
        - Or separate applications into different Maven modules.
    - Configuration example (add in `pom.xml` inside `<build><plugins>`):
      <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
      <mainClass>com.example.YourMainApplication</mainClass>
      </configuration>
      </plugin>

2. `docker build` fails in Maven stage:
    - Run `mvn -B -DskipTests package` locally to see complete errors.
    - Run `docker compose build --no-cache --progress=plain` for detailed logs.
    - Review `.dockerignore` to not exclude `src`, `pom.xml` or `mvnw`.

3. Endpoints return 401/403:
    - Confirm token, `Bearer <token>` format and that token has not expired.
    - Verify roles/authorities in security configuration.
    - Review `JWT_SECRET` and authentication configuration in `application.yml`.

4. Postgres does not accept connections:
    - Review logs: `docker compose logs -f db`
    - Check `SPRING_DATASOURCE_URL` and credentials.
    - Reset volumes if the DB is corrupted:
      docker compose down -v
      docker compose up -d --build

## Useful commands summary
- Build:
  mvn -B -DskipTests package

- Start with Docker Compose:
  docker compose up -d --build

- Logs:
  docker compose logs -f --tail=200

- Inspect ports:
  docker compose ps
  docker port <container_name> 8080

- Delete containers and volumes:
  docker compose down -v

## Security and production notes
- Do not commit secrets (`JWT_SECRET`, passwords) to the repo.
- In production use vault/secret manager and TLS.
- Review CORS, request size limits and rate limiting policies.

---  
End 