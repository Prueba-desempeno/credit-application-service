package com.CreditApplicationService.coopcredit.domain.port.inbound;


import com.CreditApplicationService.coopcredit.domain.model.Affiliate;

/**
 * Inbound port (use-case) for registering affiliates.
 */
public interface RegisterAffiliateUseCase {
    /**
     * Register a new affiliate. Implementations must validate business rules.
     * @param affiliate domain object with affiliate data
     * @return saved affiliate (with id)
     */
    Affiliate register(Affiliate affiliate);
}
