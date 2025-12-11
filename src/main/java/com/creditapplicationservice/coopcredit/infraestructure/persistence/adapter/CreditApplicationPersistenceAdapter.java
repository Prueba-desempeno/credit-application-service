package com.creditapplicationservice.coopcredit.infraestructure.persistence.adapter;

import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;
import com.creditapplicationservice.coopcredit.domain.port.outbound.CreditApplicationRepositoryPort;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.AffiliateEntity;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.CreditApplicationEntity;
import com.creditapplicationservice.coopcredit.infraestructure.mapper.CreditApplicationMapper;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.repository.AffiliateJpaRepository;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.repository.CreditApplicationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditApplicationPersistenceAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationJpaRepository repository;
    private final AffiliateJpaRepository affiliateRepo;
    private final CreditApplicationMapper mapper;

    @Override
    public CreditApplication save(CreditApplication app) {
        CreditApplicationEntity entity = mapper.toEntity(app);

        AffiliateEntity affiliateEntity =
                affiliateRepo.findById(app.getAffiliateId())
                        .orElseThrow(() -> new RuntimeException("Afiliado no encontrado"));

        entity.setAffiliate(affiliateEntity);

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CreditApplication> findByAffiliateDocument(String document) {
        return repository.findByAffiliateDocument(document)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CreditApplication> findPendingApplications() {
        return repository.findPendingApplications().stream().map(mapper::toDomain).toList();
    }
}
