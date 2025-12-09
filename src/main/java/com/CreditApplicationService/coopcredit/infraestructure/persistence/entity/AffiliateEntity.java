package com.CreditApplicationService.coopcredit.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "affiliates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String document;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double salary;

    @Column(name = "affiliation_date", nullable = false)
    private LocalDate affiliationDate;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<CreditApplicationEntity> creditApplications;
}
