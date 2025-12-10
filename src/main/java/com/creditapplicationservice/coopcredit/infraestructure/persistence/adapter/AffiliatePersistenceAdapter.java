package com.creditapplicationservice.coopcredit.infraestructure.persistence.adapter;

import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.domain.port.outbound.AffiliateRepositoryPort;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.AffiliateEntity;
import com.creditapplicationservice.coopcredit.infraestructure.mapper.AffiliateMapper;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.repository.AffiliateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AffiliatePersistenceAdapter implements AffiliateRepositoryPort {

    private final AffiliateJpaRepository repository;
    private final AffiliateMapper mapper;

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity = mapper.toEntity(affiliate);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return repository.findByDocument(document)
                .map(mapper::toDomain);
    }

    @Override
    public List<Affiliate> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
