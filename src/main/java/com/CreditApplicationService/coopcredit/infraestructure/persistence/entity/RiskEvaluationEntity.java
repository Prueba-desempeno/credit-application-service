package com.CreditApplicationService.coopcredit.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "risk_evaluations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RiskEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @Column(nullable = false)
    private String riskLevel;

    @Column(nullable = false)
    private Boolean approved;

    @Column(length = 300)
    private String reason;

    private LocalDate evaluationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false)
    private CreditApplicationEntity creditApplication;
}

