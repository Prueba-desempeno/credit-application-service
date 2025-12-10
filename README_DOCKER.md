# Docker & docker-compose

Este repo incluye un `Dockerfile` multi-stage y un `docker-compose.yml` para levantar:

- `db` (Postgres)
- `risk-central-mock` (ejecuta el profile `riskcentral`, puerto 9091)
- `credit-app` (servicio central, puerto 8080)

Cómo usar
1) Construir y levantar todo (desde la raíz del repo):

```bash
docker-compose up --build
```

2) Servicios:
- Service central: http://localhost:8080
- Mock risk: http://localhost:9091/risk-evaluation
- DB Postgres: puerto 5432 local

Notas:
- Las variables sensibles como `JWT_SECRET` están establecidas en `docker-compose.yml` para demo. No las uses en producción: usa variables de entorno o un vault.
- Flyway intentará ejecutar migraciones al iniciar el servicio central. Si quieres reiniciar la base de datos, elimina el volumen `db_data`.

Comandos útiles:
- Levantar en background: `docker-compose up -d --build`
- Ver logs: `docker-compose logs -f credit-app`
- Detener y borrar: `docker-compose down -v`


