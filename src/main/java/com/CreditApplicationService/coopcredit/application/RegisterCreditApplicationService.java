package com.CreditApplicationService.coopcredit.application;

import com.CreditApplicationService.coopcredit.domain.exception.DomainException;
import com.CreditApplicationService.coopcredit.domain.model.Affiliate;
import com.CreditApplicationService.coopcredit.domain.model.CreditApplication;
import com.CreditApplicationService.coopcredit.domain.port.inbound.RegisterCreditApplicationUseCase;
import com.CreditApplicationService.coopcredit.domain.port.outbound.AffiliateRepositoryPort;
import com.CreditApplicationService.coopcredit.domain.port.outbound.CreditApplicationRepositoryPort;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Use-case implementation for creating credit applications.
 *
 * This class applies domain validations:
 * - affiliate exists and is ACTIVE
 * - affiliate has sufficient antiquity (optional check can be here or in Evaluate use-case)
 * - amount, term and interest rate valid
 * - amount not greater than allowed by salary multiplier (this is basic guard; final evaluation happens in Evaluate service)
 *
 * Business parameters (multiplier, minAntiquity, quotaIncomeRatio) may be provided externally
 * via constructor or configuration. Here we accept a maxMultiplier parameter for a quick guard.
 */
public class RegisterCreditApplicationService implements RegisterCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditRepo;
    private final AffiliateRepositoryPort affiliateRepo;

    // Basic guard parameters (can be injected from config)
    private final double maxAmountMultiplier; // e.g., 10.0
    private final int minAntiquityMonths;     // optional guard, e.g., 6

    public RegisterCreditApplicationService(CreditApplicationRepositoryPort creditRepo,
                                            AffiliateRepositoryPort affiliateRepo,
                                            double maxAmountMultiplier,
                                            int minAntiquityMonths) {
        this.creditRepo = Objects.requireNonNull(creditRepo);
        this.affiliateRepo = Objects.requireNonNull(affiliateRepo);
        this.maxAmountMultiplier = maxAmountMultiplier;
        this.minAntiquityMonths = minAntiquityMonths;
    }

    /**
     * Create a new Credit Application with basic validations.
     */
    @Override
    public CreditApplication create(CreditApplication creditApplication) {
        if (creditApplication == null) {
            throw new DomainException("Datos de solicitud inválidos.");
        }

        if (creditApplication.getAffiliateId() == null) {
            throw new DomainException("La solicitud debe pertenecer a un afiliado.");
        }

        // Validate numbers
        if (creditApplication.getAmount() <= 0) {
            throw new DomainException("El monto debe ser mayor a 0.");
        }
        if (creditApplication.getTermMonths() <= 0) {
            throw new DomainException("El plazo debe ser mayor a 0 meses.");
        }
        if (creditApplication.getInterestRate() <= 0) {
            throw new DomainException("La tasa debe ser mayor a 0.");
        }

        // Affiliate exists
        Affiliate affiliate = affiliateRepo.findById(creditApplication.getAffiliateId())
                .orElseThrow(() -> new DomainException("Afiliado no encontrado."));

        // Affiliate active
        if (!affiliate.isActive()) {
            throw new DomainException("El afiliado debe estar ACTIVO para solicitar crédito.");
        }

        // Optional antiquity guard: if affiliate is too new, reject creation (could be relaxed)
        if (minAntiquityMonths > 0 && affiliate.monthsSinceAffiliation() < minAntiquityMonths) {
            throw new DomainException("El afiliado no cumple la antigüedad mínima requerida.");
        }

        // Guard: amount cannot exceed salary * multiplier (basic guard)
        double allowedMax = affiliate.maxAmountBySalary(maxAmountMultiplier);
        if (creditApplication.getAmount() > allowedMax) {
            throw new DomainException("El monto solicitado excede el máximo permitido por salario.");
        }

        // Set defaults: request date and status
        creditApplication.setRequestDate(LocalDateTime.now());
        creditApplication.setStatus(CreditApplication.ApplicationStatus.PENDIENTE);

        // Persist
        return creditRepo.save(creditApplication);
    }
}

