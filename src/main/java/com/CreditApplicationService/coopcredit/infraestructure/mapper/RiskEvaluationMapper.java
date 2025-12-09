package com.CreditApplicationService.coopcredit.infraestructure.mapper;

import com.CreditApplicationService.coopcredit.domain.model.RiskEvaluation;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.RiskEvaluationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {

    RiskEvaluationEntity toEntity(RiskEvaluation result);

    RiskEvaluation toDomain(RiskEvaluationEntity entity);

    // Mapeo de enums entre String y enum
    default String riskLevelToString(RiskEvaluation.RiskLevel level) {
        return level != null ? level.name() : null;
    }

    default RiskEvaluation.RiskLevel stringToRiskLevel(String level) {
        return level != null ? RiskEvaluation.RiskLevel.valueOf(level) : null;
    }
}
