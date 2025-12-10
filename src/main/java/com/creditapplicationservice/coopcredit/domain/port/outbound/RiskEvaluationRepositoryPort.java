package com.creditapplicationservice.coopcredit.domain.port.outbound;

import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;

import java.util.Optional;

/**
 * Outbound port to persist RiskEvaluation domain objects.
 */
public interface RiskEvaluationRepositoryPort {
    RiskEvaluation save(RiskEvaluation riskEvaluation);
    Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId);
    RiskEvaluation save(Long creditAppId, RiskEvaluation result);
}
