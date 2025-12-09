package com.CreditApplicationService.coopcredit.infraestructure.persistence.adapter;

import com.CreditApplicationService.coopcredit.domain.model.RiskEvaluation;
import com.CreditApplicationService.coopcredit.domain.port.outbound.RiskEvaluationRepositoryPort;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.CreditApplicationEntity;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.RiskEvaluationEntity;
import com.CreditApplicationService.coopcredit.infraestructure.mapper.RiskEvaluationMapper;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.repository.CreditApplicationJpaRepository;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.repository.RiskEvaluationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RiskEvaluationPersistenceAdapter implements RiskEvaluationRepositoryPort {

    private final RiskEvaluationJpaRepository repository;
    private final CreditApplicationJpaRepository creditRepo;
    private final RiskEvaluationMapper mapper;


    @Override
    public RiskEvaluation save(RiskEvaluation riskEvaluation) {
        RiskEvaluationEntity entity = mapper.toEntity(riskEvaluation);
        if (riskEvaluation.getCreditApplicationId() != null) {
            CreditApplicationEntity creditApp = creditRepo.findById(riskEvaluation.getCreditApplicationId())
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
            entity.setCreditApplication(creditApp);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId) {
        return repository.findByCreditApplicationId(creditApplicationId)
                .map(mapper::toDomain);
    }

    @Override
    public RiskEvaluation save(Long creditAppId, RiskEvaluation result) {

        CreditApplicationEntity creditApp =
                creditRepo.findById(creditAppId)
                        .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        RiskEvaluationEntity entity = mapper.toEntity(result);
        entity.setCreditApplication(creditApp);

        return mapper.toDomain(repository.save(entity));
    }
}
