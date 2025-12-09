package com.CreditApplicationService.coopcredit.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing a Credit Application (pure POJO).
 */
public class CreditApplication {

    private Long id;
    private Long affiliateId;
    private double amount;
    private int termMonths;
    private double interestRate;
    private LocalDateTime requestDate;
    private ApplicationStatus status; // PENDIENTE / APROBADO / RECHAZADO

    public CreditApplication() {}

    public CreditApplication(Long id, Long affiliateId, double amount, int termMonths, double interestRate, LocalDateTime requestDate, ApplicationStatus status) {
        this.id = id;
        this.affiliateId = affiliateId;
        this.amount = amount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.requestDate = requestDate;
        this.status = status;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAffiliateId() { return affiliateId; }
    public void setAffiliateId(Long affiliateId) { this.affiliateId = affiliateId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    // --- Domain helpers ---

    /**
     * Calculate approximate monthly payment using annuity formula.
     * This is a domain helper and does not access external systems.
     *
     * @return monthly installment amount
     */
    public double monthlyInstallment() {
        // monthly rate from annual interestRate (assumed as percentage)
        double monthlyRate = (interestRate / 100.0) / 12.0;
        if (monthlyRate <= 0) {
            return amount / termMonths;
        }
        double numerator = amount * monthlyRate * Math.pow(1 + monthlyRate, termMonths);
        double denominator = Math.pow(1 + monthlyRate, termMonths) - 1;
        return numerator / denominator;
    }

    public boolean isPending() {
        return status == ApplicationStatus.PENDIENTE;
    }

    public void approve() { this.status = ApplicationStatus.APROBADO; }
    public void reject() { this.status = ApplicationStatus.RECHAZADO; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditApplication)) return false;
        CreditApplication that = (CreditApplication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CreditApplication{" +
                "id=" + id +
                ", affiliateId=" + affiliateId +
                ", amount=" + amount +
                ", termMonths=" + termMonths +
                ", interestRate=" + interestRate +
                ", requestDate=" + requestDate +
                ", status=" + status +
                '}';
    }

    public enum ApplicationStatus {
        PENDIENTE,
        APROBADO,
        RECHAZADO
    }
}
