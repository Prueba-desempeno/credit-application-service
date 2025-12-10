package com.creditapplicationservice.coopcredit.domain.port.inbound;


import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;

/**
 * Inbound port for creating credit applications.
 */
public interface RegisterCreditApplicationUseCase {
    /**
     * Create a credit application (PENDING).
     * Domain validations (affiliate active, salary checks) should be applied by the implementation.
     */
    CreditApplication create(CreditApplication creditApplication);
}
