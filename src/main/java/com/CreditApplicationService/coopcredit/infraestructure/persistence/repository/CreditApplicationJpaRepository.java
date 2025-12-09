package com.CreditApplicationService.coopcredit.infraestructure.persistence.repository;

import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditApplicationJpaRepository extends JpaRepository<CreditApplicationEntity, Long> {
    List<CreditApplicationEntity> findByAffiliateDocument(String document);
    List<CreditApplicationEntity> findByStatus(String status);

    /**
     * Devuelve todas las solicitudes en estado PENDIENTE.
     */
    default List<CreditApplicationEntity> findPendingApplications() {
        return findByStatus("PENDIENTE");
    }
}
