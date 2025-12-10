package com.creditapplicationservice.coopcredit.application;

import com.creditapplicationservice.coopcredit.domain.exception.DomainException;
import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.domain.port.inbound.RegisterAffiliateUseCase;
import com.creditapplicationservice.coopcredit.domain.port.outbound.AffiliateRepositoryPort;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * Use-case implementation for registering affiliates.
 *
 * This class is framework-agnostic: it uses the AffiliateRepositoryPort to persist.
 *
 * Client-visible messages are in Spanish (DomainException messages).
 */
@Service
public class RegisterAffiliateService implements RegisterAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public RegisterAffiliateService(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = Objects.requireNonNull(affiliateRepository);
    }

    /**
     * Register a new affiliate.
     *
     * Validations:
     * - document must be unique
     * - salary > 0
     * - affiliationDate must be provided (could be set to today if null)
     *
     * @param affiliate domain object
     * @return saved affiliate
     */
    @Override
    public Affiliate register(Affiliate affiliate) {
        if (affiliate == null) {
            throw new DomainException("Datos de afiliado inválidos.");
        }

        // Document presence
        if (affiliate.getDocument() == null || affiliate.getDocument().trim().isEmpty()) {
            throw new DomainException("El documento es obligatorio.");
        }

        // Unique document
        affiliateRepository.findByDocument(affiliate.getDocument()).ifPresent(existing -> {
            throw new DomainException("Ya existe un afiliado con este documento.");
        });

        // Salary validation
        if (affiliate.getSalary() <= 0) {
            throw new DomainException("El salario debe ser mayor a 0.");
        }

        // Affiliation date: set to today if null (business decision)
        if (affiliate.getAffiliationDate() == null) {
            affiliate.setAffiliationDate(LocalDate.now());
        }

        // Default status
        if (affiliate.getStatus() == null) {
            affiliate.setStatus(Affiliate.AffiliateStatus.ACTIVO);
        }

        // Persist and return
        return affiliateRepository.save(affiliate);
    }
}
