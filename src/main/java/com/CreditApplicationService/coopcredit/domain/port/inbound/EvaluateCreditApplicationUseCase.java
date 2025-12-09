package com.CreditApplicationService.coopcredit.domain.port.inbound;


/**
 * Inbound port (use-case) for evaluating a credit application.
 */
public interface EvaluateCreditApplicationUseCase {
    /**
     * Evaluate a credit application by id.
     * The implementation must be transactional and must persist the evaluation result.
     * @param creditApplicationId id of the application to evaluate
     */
    void evaluate(Long creditApplicationId);
}
