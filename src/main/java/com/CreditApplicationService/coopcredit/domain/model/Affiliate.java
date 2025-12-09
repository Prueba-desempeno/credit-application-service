package com.CreditApplicationService.coopcredit.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Domain model representing an Affiliate (pure POJO, no framework annotations).
 */
public class Affiliate {

    private Long id;
    private String document;
    private String name;
    private double salary;
    private LocalDate affiliationDate;
    private AffiliateStatus status; // ACTIVO / INACTIVO

    public Affiliate() {}

    public Affiliate(Long id, String document, String name, double salary, LocalDate affiliationDate, AffiliateStatus status) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.salary = salary;
        this.affiliationDate = affiliationDate;
        this.status = status;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public LocalDate getAffiliationDate() { return affiliationDate; }
    public void setAffiliationDate(LocalDate affiliationDate) { this.affiliationDate = affiliationDate; }

    public AffiliateStatus getStatus() { return status; }
    public void setStatus(AffiliateStatus status) { this.status = status; }

    // --- Domain helper methods ---

    /**
     * Check if the affiliate is active.
     */
    public boolean isActive() {
        return status == AffiliateStatus.ACTIVO;
    }

    /**
     * Returns the number of full months since affiliation.
     */
    public int monthsSinceAffiliation() {
        if (affiliationDate == null) return 0;
        return Period.between(affiliationDate, LocalDate.now()).getMonths()
                + Period.between(affiliationDate, LocalDate.now()).getYears() * 12;
    }

    /**
     * Simple business helper: maximum allowed loan amount based on salary and multiplier.
     * This is domain-level logic and can be used by use-cases (configurable multiplier kept outside domain).
     */
    public double maxAmountBySalary(double multiplier) {
        return this.salary * multiplier;
    }

    /**
     * Check if the affiliate has minimum antiquity.
     */
    public boolean hasMinimumAntiquity(int months) {
        return affiliationDate != null && affiliationDate.plusMonths(months).isBefore(LocalDate.now());
    }

    /**
     * Update affiliate data.
     */
    public void updateData(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Affiliate)) return false;
        Affiliate that = (Affiliate) o;
        return Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document);
    }

    @Override
    public String toString() {
        return "Affiliate{" +
                "id=" + id +
                ", document='" + document + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", affiliationDate=" + affiliationDate +
                ", status=" + status +
                '}';
    }

    // Enum for status kept inside domain for purity
    public enum AffiliateStatus {
        ACTIVO,
        INACTIVO
    }
}
