package com.creditapplicationservice.coopcredit.domain.port.outbound;

import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for credit application persistence.
 */
public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication creditApplication);
    Optional<CreditApplication> findById(Long id);

    List<CreditApplication> findByAffiliateDocument(String document);

    List<CreditApplication> findPendingApplications();
}
