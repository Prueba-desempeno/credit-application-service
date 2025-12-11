package com.creditapplicationservice.coopcredit.infraestructure.adapter;

import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationPort;
import com.creditapplicationservice.coopcredit.riskcentral.dto.RiskRequest;
import com.creditapplicationservice.coopcredit.riskcentral.dto.RiskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Component
@Primary
public class RiskCentralRestAdapter implements RiskEvaluationPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String riskServiceUrl;

    public RiskCentralRestAdapter(@Value("${risk.central.url:}") String riskServiceUrl) {
        this.riskServiceUrl = riskServiceUrl;
    }

    @Override
    public RiskEvaluation evaluateRisk(String document, double amount, int termMonths) {
        if (riskServiceUrl == null || riskServiceUrl.isBlank()) {
            // No URL configured, fallback to local mock adapter behaviour
            int seed = Math.abs(document.hashCode()) % 1000;
            int score = 300 + (int) ((950 - 300) * (seed / 999.0));
            RiskEvaluation r = new RiskEvaluation();
            r.setScore(score);
            if (score <= 500) r.setRiskLevel(RiskEvaluation.RiskLevel.ALTO);
            else if (score <= 700) r.setRiskLevel(RiskEvaluation.RiskLevel.MEDIO);
            else r.setRiskLevel(RiskEvaluation.RiskLevel.BAJO);
            r.setDetail("Fallback local: evaluación simulada para documento: " + document);
            return r;
        }

        String url = riskServiceUrl.endsWith("/") ? riskServiceUrl + "risk/risk-evaluation" : riskServiceUrl + "/risk/risk-evaluation";
        RiskRequest req = new RiskRequest();
        req.setDocumento(document);
        req.setMonto(amount);
        req.setPlazo(termMonths);
        try {
            ResponseEntity<RiskResponse> resp = restTemplate.postForEntity(url, req, RiskResponse.class);
            RiskResponse body = resp.getBody();
            if (body == null) return null;
            RiskEvaluation r = new RiskEvaluation();
            r.setScore(body.getScore());
            String nivel = body.getNivelRiesgo();
            if ("ALTO".equalsIgnoreCase(nivel)) r.setRiskLevel(RiskEvaluation.RiskLevel.ALTO);
            else if ("MEDIO".equalsIgnoreCase(nivel)) r.setRiskLevel(RiskEvaluation.RiskLevel.MEDIO);
            else r.setRiskLevel(RiskEvaluation.RiskLevel.BAJO);
            r.setDetail(body.getDetalle());
            return r;
        } catch (RestClientException e) {
            // fallback to local deterministic logic
            int seed = Math.abs(document.hashCode()) % 1000;
            int score = 300 + (int) ((950 - 300) * (seed / 999.0));
            RiskEvaluation r = new RiskEvaluation();
            r.setScore(score);
            if (score <= 500) r.setRiskLevel(RiskEvaluation.RiskLevel.ALTO);
            else if (score <= 700) r.setRiskLevel(RiskEvaluation.RiskLevel.MEDIO);
            else r.setRiskLevel(RiskEvaluation.RiskLevel.BAJO);
            r.setDetail("Fallback after error: " + e.getMessage());
            return r;
        }
    }
}
