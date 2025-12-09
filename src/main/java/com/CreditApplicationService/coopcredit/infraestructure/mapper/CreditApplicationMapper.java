package com.CreditApplicationService.coopcredit.infraestructure.mapper;

import com.CreditApplicationService.coopcredit.domain.model.CreditApplication;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.CreditApplicationEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AffiliateMapper.class, RiskEvaluationMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "affiliate", ignore = true) // We set affiliate manually
    CreditApplicationEntity toEntity(CreditApplication app);

    @Mapping(target = "affiliateId", source = "affiliate.id")
    CreditApplication toDomain(CreditApplicationEntity entity);

    // Mapeo de enums entre String y enum
    default String statusToString(CreditApplication.ApplicationStatus status) {
        return status != null ? status.name() : null;
    }

    default CreditApplication.ApplicationStatus stringToStatus(String status) {
        return status != null ? CreditApplication.ApplicationStatus.valueOf(status) : null;
    }
}
