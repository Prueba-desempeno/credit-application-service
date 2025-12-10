package com.creditapplicationservice.coopcredit.infraestructure.adapter;

import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationPort;
import org.springframework.stereotype.Component;

/**
 * Simple adapter implementing RiskEvaluationPort for local tests.
 * It deterministically generates a score based on document hash so results are repeatable.
 */
@Component
public class RiskCentralMockAdapter implements RiskEvaluationPort {

    @Override
    public RiskEvaluation evaluateRisk(String document, double amount, int termMonths) {
        int seed = Math.abs(document.hashCode()) % 1000; // 0-999
        int score = 300 + (int) ((950 - 300) * (seed / 999.0));
        RiskEvaluation.RiskLevel level;
        if (score <= 500) level = RiskEvaluation.RiskLevel.ALTO;
        else if (score <= 700) level = RiskEvaluation.RiskLevel.MEDIO;
        else level = RiskEvaluation.RiskLevel.BAJO;

        RiskEvaluation r = new RiskEvaluation();
        r.setScore(score);
        r.setRiskLevel(level);
        r.setDetail("Evaluación simulada (mock) para documento: " + document);
        return r;
    }
}
