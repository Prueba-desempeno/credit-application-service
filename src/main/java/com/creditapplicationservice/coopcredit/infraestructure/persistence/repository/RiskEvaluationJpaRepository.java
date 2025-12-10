package com.creditapplicationservice.coopcredit.infraestructure.persistence.repository;

import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.RiskEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiskEvaluationJpaRepository extends JpaRepository<RiskEvaluationEntity, Long> {
    Optional<RiskEvaluationEntity> findByCreditApplicationId(Long creditApplicationId);
}
