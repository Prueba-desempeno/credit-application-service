package com.creditapplicationservice.coopcredit.infraestructure.rest.dto;

import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AffiliateRequest {

    private String document;
    private String name;
    private double salary;
    private LocalDate affiliationDate; // nuevo campo opcional

    public Affiliate toDomain() {
        Affiliate affiliate = new Affiliate();
        affiliate.setDocument(document);
        affiliate.setName(name);
        affiliate.setSalary(salary);
        affiliate.setAffiliationDate(affiliationDate != null ? affiliationDate : LocalDate.now());
        affiliate.setStatus(Affiliate.AffiliateStatus.ACTIVO);
        return affiliate;
    }
}
