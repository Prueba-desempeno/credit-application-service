package com.creditapplicationservice.coopcredit.infraestructure.persistence.repository;

import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AffiliateJpaRepository extends JpaRepository<AffiliateEntity, Long> {
    Optional<AffiliateEntity> findByDocument(String document);
}

