# risk-central-mock-service (simulado)

Este proyecto incluye dentro del mismo repo una simulación ligera del microservicio `risk-central-mock-service`.

Cómo ejecutar el mock standalone (usar un puerto distinto):

1. Construye el proyecto:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
mvn -U clean package -DskipTests
```

2. Ejecuta el mock en el perfil `riskcentral` (usa `application-riskcentral.properties`, puerto 9091):

```bash
java -jar target/credit-application-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=riskcentral
```

3. Endpoint disponible (en el mock):

POST http://localhost:9091/risk-evaluation

Body example:

```json
{
  "documento": "1017654311",
  "monto": 5000000,
  "plazo": 36
}
```

Respuesta típica:

```json
{
  "documento": "1017654311",
  "score": 642,
  "nivelRiesgo": "MEDIO",
  "detalle": "Historial crediticio moderado."
}
```

Cómo probar integración con el servicio central (en el mismo jar):

- Configura la propiedad `risk.central.url=http://localhost:9091` en el `application.properties` o exporta la variable de entorno `RISK_CENTRAL_URL` antes de iniciar el servicio central.

- Inicia el servicio central (por defecto puerto 8080) y crea una solicitud de crédito desde un afiliado para disparar la evaluación. El adapter `RiskCentralRestAdapter` consultará el endpoint `POST /risk-evaluation` del mock.

Nota: el proyecto ya incluye un `RiskCentralRestAdapter` que consulta la URL configurada y hace fallback a un algoritmo determinista si la URL no está configurada o falla.
