package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;

import com.CreditApplicationService.coopcredit.domain.model.CreditApplication;
import lombok.Data;

@Data
public class CreditApplicationRequest {

    private Long affiliateId;
    private double amount;
    private int termMonths;
    private double interestRate;

    public CreditApplication toDomain() {
        CreditApplication c = new CreditApplication();
        c.setAffiliateId(affiliateId);
        c.setAmount(amount);
        c.setTermMonths(termMonths);
        c.setInterestRate(interestRate);
        return c;
    }
}
