# Docker & docker-compose

This repository contains a Spring Boot service (credit-app), a third-party mock (risk-central-mock), and a Postgres database. This document explains how to set up, build, run, and debug using Docker / Docker Compose.

## Service Summary
- `credit-app` — Main service (default port 8080 inside the container).
- `risk-central-mock` — Risk service mock (default port 9091 inside the container).
- `db` — Postgres (port 5432).

The final names and ports depend on `docker-compose.yml`.

## Requirements
- Docker Engine and Docker Compose (v2+).
- JDK 17 / Maven 3.6+ (only if compiling locally).
- `curl` or Postman for testing endpoints.
- (Optional) `mvnw` included in the repo for reproducible builds.

## Important environment variables
Usually defined in `docker-compose.yml` or in an `.env` file:
- `SPRING_DATASOURCE_URL` (e.g.: `jdbc:postgresql://db:5432/creditdb`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT` (if the app supports it)
- `JWT_SECRET` (do not leave in plain text in production)

Review `docker-compose.yml` for default environment values.

## How to build and lift (from the repo root)

1) Lift in the foreground (view logs in console):

   docker-compose up --build

2) Lift in the background (detached):

   docker-compose up -d --build

3) Stop and delete containers and data volumes:

   docker-compose down -v

Note: If your Docker version uses the subcommand without a hyphen, use `docker compose up --build` and `docker compose down -v`.

## Useful commands for diagnostics

- View status and ports:

  docker-compose ps

- View real-time logs (all services):

  docker-compose logs -f --tail=200

- View logs for a specific service (e.g.: credit-app):

  docker-compose logs -f credit-app --tail=200

- Inspect mapped port:

  docker port <container_name> 8080

- Execute a bash command in a service:

  docker-compose exec credit-app /bin/sh

- Execute pg_isready in the Postgres container:

  docker-compose exec db pg_isready -U <user>

## Local Build (without Docker)
To check for compilation failures before building the image:

    mvn -B -DskipTests package

Or use the wrapper:

    ./mvnw -B -DskipTests package

It is also possible to reproduce the build inside a Maven container:

    docker run --rm -v "$PWD":/workspace -w /workspace maven:3.8.8-eclipse-temurin-17 mvn -B -DskipTests package

## Recommendations for the `Dockerfile` (multistage)
- Copy `mvnw`, `.mvn`, and `pom.xml` before the rest to leverage cache.
- Copy the entire project and run the package in the builder stage.
- In the runtime stage, copy `target/*.jar` as `app.jar` to avoid dependencies on the exact name.

Example (summary of steps):

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

Make sure not to exclude necessary files with `.dockerignore`.

## Common Problem: Maven cannot find a single `main` class
Typical message:

    Unable to find a single main class from the following candidates [...]

Cause: There is more than one class with `@SpringBootApplication` in the same artifact. Solutions:

- Explicitly define the main class in `pom.xml` for the `spring-boot-maven-plugin` (if the image should contain only one of the applications). Example (add it inside `<build><plugins>`):

    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <mainClass>com.creditapplicationservice.coopcredit.riskcentral.RiskCentralMockApplication</mainClass>
        </configuration>
    </plugin>

- Alternative: Separate applications into different Maven modules (each module with its own `@SpringBootApplication`).

After defining `mainClass`, rebuild:

    mvn -B -DskipTests package
    docker-compose build --no-cache --progress=plain

## How to verify that everything is working
1. View containers and ports:

   docker-compose ps

2. Look for the line `Started ... in ... seconds` in the logs:

   docker-compose logs -f credit-app --tail=200

3. Test the health endpoint:

   curl -v http://localhost:8080/actuator/health

(Adjust port according to the mapping shown by `docker-compose ps`.)

4. Test API endpoints with Postman or curl (examples in the Postman collection).

## Postman Collection
An example collection `postman_collection_credit_app.json` is included (import into Postman). Recommended flow:

- `Register User` -> `Login User` (script saves `access_token`)
- Execute authenticated requests with the header:

  Authorization: Bearer {{access_token}}

Make sure to create an Environment in Postman with `base_url` (default `http://localhost:8080`).

## Quick Troubleshooting

- Maven build fails inside Docker:
    - Run `mvn -B -DskipTests package` locally to see the full error.
    - Run `docker-compose build --no-cache --progress=plain` for detailed logs.
    - Check `.dockerignore` to ensure you are not excluding `src`, `pom.xml`, or `mvnw`.

- The app starts but returns 401/403 on protected endpoints:
    - Confirm you are using the correct token (check auth service logs).
    - Verify roles and routes in the security configuration.
    - Check `JWT_SECRET` and expiration dates.

- Postgres does not accept connections:
    - Check db service logs: `docker-compose logs -f db`
    - Verify `SPRING_DATASOURCE_URL` in `docker-compose.yml`
    - Run `docker-compose exec db pg_isready -U <user>`

## Reset database data
To delete data persisted in volumes and restart:

    docker-compose down -v
    docker-compose up --build -d

## Security Notes
- Do not store secrets (e.g., `JWT_SECRET`, passwords) in `docker-compose.yml` for uncontrolled environments.
- In production, use a vault or environment variables from the deployment environment.

## Changelog (brief)
- Documentation updated for build steps, multistage Dockerfile, solution for the `mainClass` error, and testing with Postman.

End.
