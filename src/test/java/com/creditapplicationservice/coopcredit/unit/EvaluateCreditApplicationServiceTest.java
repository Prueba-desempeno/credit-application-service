package com.creditapplicationservice.coopcredit.unit;

import com.creditapplicationservice.coopcredit.application.EvaluateCreditApplicationService;
import com.creditapplicationservice.coopcredit.domain.exception.DomainException;
import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;
import com.creditapplicationservice.coopcredit.domain.model.RiskEvaluation;
import com.creditapplicationservice.coopcredit.domain.port.outbound.AffiliateRepositoryPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.CreditApplicationRepositoryPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationPort;
import com.creditapplicationservice.coopcredit.domain.port.outbound.RiskEvaluationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EvaluateCreditApplicationServiceTest {

    private CreditApplicationRepositoryPort creditRepo;
    private AffiliateRepositoryPort affiliateRepo;
    private RiskEvaluationPort riskPort;
    private RiskEvaluationRepositoryPort riskRepo;
    private EvaluateCreditApplicationService service;

    @BeforeEach
    void setup() {
        creditRepo = Mockito.mock(CreditApplicationRepositoryPort.class);
        affiliateRepo = Mockito.mock(AffiliateRepositoryPort.class);
        riskPort = Mockito.mock(RiskEvaluationPort.class);
        riskRepo = Mockito.mock(RiskEvaluationRepositoryPort.class);

        service = new EvaluateCreditApplicationService(creditRepo, affiliateRepo, riskPort, riskRepo, 10.0, 6, 0.4);
    }

    @Test
    void evaluate_pendingAndLowRisk_approve() {
        CreditApplication app = new CreditApplication();
        app.setId(1L);
        app.setAffiliateId(2L);
        app.setAmount(1000);
        app.setTermMonths(12);
        app.setInterestRate(5.0);
        app.setRequestDate(LocalDateTime.now());
        app.setStatus(CreditApplication.ApplicationStatus.PENDIENTE);

        Affiliate aff = new Affiliate();
        aff.setId(2L);
        aff.setSalary(10000);
        aff.setAffiliationDate(java.time.LocalDate.now().minusMonths(12));
        aff.setStatus(Affiliate.AffiliateStatus.ACTIVO);

        when(creditRepo.findById(1L)).thenReturn(Optional.of(app));
        when(affiliateRepo.findById(2L)).thenReturn(Optional.of(aff));

        RiskEvaluation re = new RiskEvaluation();
        re.setScore(800);
        re.setRiskLevel(RiskEvaluation.RiskLevel.BAJO);
        when(riskPort.evaluateRisk(any(), any(Double.class), any(Integer.class))).thenReturn(re);
        when(riskRepo.save(any(RiskEvaluation.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> service.evaluate(1L));
    }

    @Test
    void evaluate_nonPending_throws() {
        CreditApplication app = new CreditApplication();
        app.setId(1L);
        app.setAffiliateId(2L);
        app.setStatus(CreditApplication.ApplicationStatus.APROBADO);
        when(creditRepo.findById(1L)).thenReturn(Optional.of(app));

        DomainException ex = assertThrows(DomainException.class, () -> service.evaluate(1L));
        assertTrue(ex.getMessage().contains("PENDIENTE"));
    }
}

