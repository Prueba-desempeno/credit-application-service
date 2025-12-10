# Build stage
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copiar archivos necesarios para aprovechar cache; usar mvnw si está presente
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
# Copiar todo el proyecto (asegúrate de no excluir con .dockerignore)
COPY . .

# Ejecutar build con el wrapper si existe, fallback a mvn
RUN if [ -x "./mvnw" ]; then ./mvnw -B -DskipTests package; else mvn -B -DskipTests package; fi

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


