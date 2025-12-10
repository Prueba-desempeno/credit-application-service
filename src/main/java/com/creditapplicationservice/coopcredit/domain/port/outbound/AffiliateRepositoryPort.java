package com.creditapplicationservice.coopcredit.domain.port.outbound;

import com.creditapplicationservice.coopcredit.domain.model.Affiliate;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for affiliate persistence operations.
 */
public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByDocument(String document);
    List<Affiliate> findAll();
}
