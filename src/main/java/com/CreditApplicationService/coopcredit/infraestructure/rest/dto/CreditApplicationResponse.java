package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;

import com.CreditApplicationService.coopcredit.domain.model.CreditApplication;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditApplicationResponse {

    private Long id;
    private Long affiliateId;
    private double amount;
    private int termMonths;
    private double interestRate;
    private LocalDate requestDate;
    private String status;
    private Long evaluationId;

    public static CreditApplicationResponse fromDomain(CreditApplication c) {
        CreditApplicationResponse r = new CreditApplicationResponse();
        r.setId(c.getId());
        r.setAffiliateId(c.getAffiliateId());
        r.setAmount(c.getAmount());
        r.setTermMonths(c.getTermMonths());
        r.setInterestRate(c.getInterestRate());
        r.setRequestDate(c.getRequestDate());
        r.setStatus(c.getStatus());
        if (c.getEvaluation() != null) {
            r.setEvaluationId(c.getEvaluation().getId());
        }
        return r;
    }
}
