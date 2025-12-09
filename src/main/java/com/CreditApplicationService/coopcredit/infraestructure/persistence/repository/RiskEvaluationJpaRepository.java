package com.CreditApplicationService.coopcredit.infraestructure.persistence.repository;

import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.RiskEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskEvaluationJpaRepository extends JpaRepository<RiskEvaluationEntity, Long> {
}
