package com.creditapplicationservice.coopcredit.infraestructure.rest.dto;

import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;
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
        // Convert LocalDateTime -> LocalDate safely
        if (c.getRequestDate() != null) {
            r.setRequestDate(c.getRequestDate().toLocalDate());
        }
        // status as String (enum)
        if (c.getStatus() != null) {
            r.setStatus(c.getStatus().name());
        }
        // evaluation relationship is stored externally; leave evaluationId null here
        // Adapter or controller can populate it if needed
        return r;
    }
}
