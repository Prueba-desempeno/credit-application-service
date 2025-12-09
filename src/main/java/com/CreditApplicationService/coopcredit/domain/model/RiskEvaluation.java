package com.CreditApplicationService.coopcredit.domain.model;

import java.util.Objects;

/**
 * Domain model representing the result of a risk evaluation.
 */
public class RiskEvaluation {

    private Long id;
    private Long creditApplicationId;
    private int score; // 300 - 950
    private RiskLevel riskLevel; // ALTO / MEDIO / BAJO
    private String detail;

    public RiskEvaluation() {}

    public RiskEvaluation(Long id, Long creditApplicationId, int score, RiskLevel riskLevel, String detail) {
        this.id = id;
        this.creditApplicationId = creditApplicationId;
        this.score = score;
        this.riskLevel = riskLevel;
        this.detail = detail;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCreditApplicationId() { return creditApplicationId; }
    public void setCreditApplicationId(Long creditApplicationId) { this.creditApplicationId = creditApplicationId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RiskEvaluation)) return false;
        RiskEvaluation that = (RiskEvaluation) o;
        return Objects.equals(creditApplicationId, that.creditApplicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditApplicationId);
    }

    @Override
    public String toString() {
        return "RiskEvaluation{" +
                "id=" + id +
                ", creditApplicationId=" + creditApplicationId +
                ", score=" + score +
                ", riskLevel=" + riskLevel +
                ", detail='" + detail + '\'' +
                '}';
    }

    public enum RiskLevel {
        ALTO,
        MEDIO,
        BAJO
    }

    public boolean isApproved() {
        return riskLevel == RiskLevel.BAJO || (riskLevel == RiskLevel.MEDIO && score > 500);
    }
}
