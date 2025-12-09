package com.CreditApplicationService.coopcredit.infraestructure.rest.dto;

import com.CreditApplicationService.coopcredit.domain.model.Affiliate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AffiliateRequest {

    private String document;
    private String name;
    private double salary;

    public Affiliate toDomain() {
        Affiliate affiliate = new Affiliate();
        affiliate.setDocument(document);
        affiliate.setName(name);
        affiliate.setSalary(salary);
        affiliate.setAffiliationDate(LocalDate.now());
        affiliate.setStatus(Affiliate.AffiliateStatus.ACTIVO);
        return affiliate;
    }
}
