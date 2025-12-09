package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;


import com.CreditApplicationService.coopcredit.domain.model.Affiliate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AffiliateResponse {

    private Long id;
    private String document;
    private String name;
    private double salary;
    private LocalDate affiliationDate;
    private String status;

    public static AffiliateResponse fromDomain(Affiliate a) {
        AffiliateResponse r = new AffiliateResponse();
        r.setId(a.getId());
        r.setDocument(a.getDocument());
        r.setName(a.getName());
        r.setSalary(a.getSalary());
        r.setAffiliationDate(a.getAffiliationDate());
        r.setStatus(a.getStatus());
        return r;
    }
}

