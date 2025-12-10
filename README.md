# Credit Application Service (CoopCredit)

Proyecto con arquitectura hexagonal que implementa un servicio de solicitudes de crédito (`credit-application-service`) y un microservicio simulado de riesgo (`risk-central-mock-service`).

Contenido:
- `src/main/java/...` - código del servicio central y mock
- `src/main/resources` - properties, migraciones Flyway
- `Dockerfile` - multi-stage para construir imagen
- `docker-compose.yml` - orquesta Postgres, mock y servicio central

Cómo ejecutar con Docker Compose (desarrollo local):

```bash
# build and run
docker-compose up --build

# credit app -> http://localhost:8080
# risk mock -> http://localhost:9091/risk-evaluation
```

Run tests locally (requires Java 17):

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
mvn -U clean test
```

Notes:
- Configure `JWT_SECRET` env var or `jwt.secret` property for production.
- The `docker-compose.yml` sets example credentials for Postgres; change them in production.

