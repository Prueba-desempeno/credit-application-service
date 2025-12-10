package com.creditapplicationservice.coopcredit.infraestructure.rest.dto;

import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;
import lombok.Data;

@Data
public class RiskEvaluationResponse {

    private Long id;
    private int score;
    private String riskLevel;
    private String details;

    public static RiskEvaluationResponse fromDomain(RiskEvaluation e) {
        RiskEvaluationResponse r = new RiskEvaluationResponse();
        r.setId(e.getId());
        r.setScore(e.getScore());
        r.setRiskLevel(e.getRiskLevel() != null ? e.getRiskLevel().name() : null);
        r.setDetails(e.getDetail());
        return r;
    }
}
