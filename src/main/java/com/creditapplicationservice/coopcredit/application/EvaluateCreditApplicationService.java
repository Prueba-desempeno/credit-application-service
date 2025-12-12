package com.creditapplicationservice.coopcredit.application;

import com.creditapplicationservice.coopcredit.domain.exception.DomainException;
import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;
import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;
import com.creditapplicationservice.coopcredit.domain.port.inbound.EvaluateCreditApplicationUseCase;
import com.creditapplicationservice.coopcredit.domain.port.outbound.AffiliateRepositoryPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.CreditApplicationRepositoryPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * EvaluateCreditApplicationService:
 * - transactional evaluation flow
 * - calls external RiskEvaluationPort
 * - persists RiskEvaluation via RiskEvaluationRepositoryPort
 * - updates CreditApplication status accordingly
 *
 * Decision rules implemented:
 * - If risk level ALTO -> RECHAZADO
 * - If risk level MEDIO -> check quota/income and max amount; if fails -> RECHAZADO else -> APROBADO
 * - If risk level BAJO -> APROBADO (still checks quota/income as extra guard)
 *
 * Business parameters are injected via constructor.
 */
@Service
public class EvaluateCreditApplicationService implements EvaluateCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditRepo;
    private final AffiliateRepositoryPort affiliateRepo;
    private final RiskEvaluationPort riskPort; // external service adapter
    private final RiskEvaluationRepositoryPort riskRepo; // persistence for evaluation

    // business rules params
    private final double maxAmountMultiplier;       // e.g., 10.0
    private final int minAntiquityMonths;           // e.g., 6
    private final double maxQuotaIncomeRatio;       // e.g., 0.4

    public EvaluateCreditApplicationService(CreditApplicationRepositoryPort creditRepo,
                                            AffiliateRepositoryPort affiliateRepo,
                                            RiskEvaluationPort riskPort,
                                            RiskEvaluationRepositoryPort riskRepo,
                                            @Value("${business.maxAmountMultiplier:10.0}") double maxAmountMultiplier,
                                            @Value("${business.minAntiquityMonths:6}") int minAntiquityMonths,
                                            @Value("${business.maxQuotaIncomeRatio:0.4}") double maxQuotaIncomeRatio) {
        this.creditRepo = Objects.requireNonNull(creditRepo);
        this.affiliateRepo = Objects.requireNonNull(affiliateRepo);
        this.riskPort = Objects.requireNonNull(riskPort);
        this.riskRepo = Objects.requireNonNull(riskRepo);
        this.maxAmountMultiplier = maxAmountMultiplier;
        this.minAntiquityMonths = minAntiquityMonths;
        this.maxQuotaIncomeRatio = maxQuotaIncomeRatio;
    }

    /**
     * Evaluate a credit application. Entire flow runs inside a transaction to keep consistency.
     *
     * @param creditApplicationId id of the application to evaluate
     */
    @Override
    @Transactional
    public String evaluate(Long creditApplicationId) {
        if (creditApplicationId == null) {
            throw new DomainException("Id de solicitud inválido.");
        }

        // 1) Load credit application
        CreditApplication application = creditRepo.findById(creditApplicationId)
                .orElseThrow(() -> new DomainException("Solicitud no encontrada."));

        // Only pending apps can be evaluated
        if (!application.isPending()) {
            throw new DomainException("Solo se pueden evaluar solicitudes en estado PENDIENTE.");
        }

        // 2) Load affiliate
        Affiliate affiliate = affiliateRepo.findById(application.getAffiliateId())
                .orElseThrow(() -> new DomainException("Afiliado asociado no encontrado."));

        // 3) Business guards: affiliate active and antiquity
        if (!affiliate.isActive()) {
            throw new DomainException("El afiliado debe estar ACTIVO para evaluar la solicitud.");
        }
        if (minAntiquityMonths > 0 && affiliate.monthsSinceAffiliation() < minAntiquityMonths) {
            application.setStatus(CreditApplication.ApplicationStatus.RECHAZADO);
            creditRepo.save(application);
            throw new DomainException("El afiliado no cumple la antigüedad mínima requerida.");
        }

        // 4) Call external risk service
        RiskEvaluation externalEvaluation = riskPort.evaluateRisk(affiliate.getDocument(), application.getAmount(), application.getTermMonths());
        if (externalEvaluation == null) {
            throw new DomainException("Error al evaluar riesgo externo.");
        }

        // 5) Persist evaluation (link with creditApplicationId)
        RiskEvaluation evaluationToSave = new RiskEvaluation();
        evaluationToSave.setCreditApplicationId(application.getId());
        evaluationToSave.setScore(externalEvaluation.getScore());
        evaluationToSave.setRiskLevel(externalEvaluation.getRiskLevel());
        evaluationToSave.setDetail(externalEvaluation.getDetail());

        RiskEvaluation savedEval = riskRepo.save(evaluationToSave);

        // 6) Apply internal policies to decide approval
        boolean approved = decideApproval(application, affiliate, savedEval);

        // 7) Update application status and persist
        application.setStatus(approved ? CreditApplication.ApplicationStatus.APROBADO : CreditApplication.ApplicationStatus.RECHAZADO);
        creditRepo.save(application);
        return application.getStatus().name();
    }

    /**
     * Decision algorithm combining external risk and internal business checks.
     * Returns true if approved, false if rejected.
     */
    private boolean decideApproval(CreditApplication application, Affiliate affiliate, RiskEvaluation evaluation) {
        // If ALTO risk -> reject
        if (evaluation.getRiskLevel() == RiskEvaluation.RiskLevel.ALTO) {
            return false;
        }

        // Check max amount by salary
        double allowedMax = affiliate.maxAmountBySalary(maxAmountMultiplier);
        if (application.getAmount() > allowedMax) {
            return false;
        }

        // Check quota / income ratio
        double monthlyInstallment = application.monthlyInstallment();
        if (affiliate.getSalary() <= 0) {
            return false; // defensive
        }
        double ratio = monthlyInstallment / affiliate.getSalary();
        if (ratio > maxQuotaIncomeRatio) {
            return false;
        }

        // For MEDIO risk we already checked quotas and amount; if passed -> approve
        if (evaluation.getRiskLevel() == RiskEvaluation.RiskLevel.MEDIO) {
            return true;
        }

        // BAJO risk: approve
        return evaluation.getRiskLevel() == RiskEvaluation.RiskLevel.BAJO;
    }
}
