package com.CreditApplicationService.coopcredit.domain.port.outbound;

import com.CreditApplicationService.coopcredit.domain.model.RiskEvaluation;

/**
 * Outbound port to obtain a risk score from external system (risk central).
 * Implementations should call an external REST service and map response to domain model or primitive values.
 */
public interface RiskEvaluationPort {
    /**
     * Evaluate risk using external system and return a RiskEvaluation domain object (score + level + detail).
     *
     * @param document applicant's document
     * @param amount requested amount
     * @param termMonths term in months
     * @return RiskEvaluation with score and level
     */
    RiskEvaluation evaluateRisk(String document, double amount, int termMonths);
}
