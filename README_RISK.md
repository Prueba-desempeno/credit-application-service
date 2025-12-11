# risk-central-mock-service (Simulated)

This project includes a lightweight simulation of the `risk-central-mock-service` microservice within the same repository.

How to run the mock standalone (using a different port):

1. Build the project:
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
mvn -U clean package -DskipTests
```

1. Run the mock using the `riskcentral` profile (uses `application-riskcentral.properties`, port 9091):
   Bash
```
java -jar target/credit-application-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=riskcentral
```

1. Available Endpoint (on the mock):
   POST http://localhost:9091/risk-evaluation
   Body example:
   JSON
```
{
  "documento": "1017654311",
  "monto": 5000000,
  "plazo": 36
}
```

Typical Response:
JSON
```
{
  "documento": "1017654311",
  "score": 642,
  "nivelRiesgo": "MEDIO",
  "detalle": "Historial crediticio moderado."
}
```

How to test integration with the central service (in the same jar):
* Configure the property `risk.central.url=http://localhost:9091` in `application.properties` or export the environment variable `RISK_CENTRAL_URL` before starting the central service.
* Start the central service (default port 8080) and create a credit application from an affiliate to trigger the evaluation. The adapter `RiskCentralRestAdapter` will query the mock's `POST /risk-evaluation` endpoint.
  Note: The project already includes a `RiskCentralRestAdapter` which queries the configured URL and falls back to a deterministic algorithm if the URL is not configured or fails.