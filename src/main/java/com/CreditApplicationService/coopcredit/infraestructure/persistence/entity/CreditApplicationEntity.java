package com.CreditApplicationService.coopcredit.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "credit_applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreditApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private AffiliateEntity affiliate;

    @Column(nullable = false)
    private Double requestedAmount;

    @Column(nullable = false)
    private Integer termMonths;

    @Column(nullable = false)
    private Double proposedRate;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false)
    private String status;

    @OneToOne(mappedBy = "creditApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RiskEvaluationEntity riskEvaluation;
}

