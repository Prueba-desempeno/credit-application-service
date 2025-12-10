package com.creditapplicationservice.coopcredit.infraestructure.mapper;

import com.creditapplicationservice.coopcredit.domain.model.CreditApplication;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.CreditApplicationEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {AffiliateMapper.class, RiskEvaluationMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "affiliate", ignore = true) // We set affiliate manually in adapter
    @Mapping(target = "requestedAmount", source = "amount")
    @Mapping(target = "proposedRate", source = "interestRate")
    @Mapping(target = "termMonths", source = "termMonths")
    @Mapping(target = "requestDate", source = "requestDate", qualifiedByName = "localDateTimeToLocalDate")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    CreditApplicationEntity toEntity(CreditApplication app);

    @Mapping(target = "affiliateId", source = "affiliate.id")
    @Mapping(target = "amount", source = "requestedAmount")
    @Mapping(target = "interestRate", source = "proposedRate")
    @Mapping(target = "termMonths", source = "termMonths")
    @Mapping(target = "requestDate", source = "requestDate", qualifiedByName = "localDateToLocalDateTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    CreditApplication toDomain(CreditApplicationEntity entity);

    // Mapeo de enums entre String y enum
    @Named("statusToString")
    default String statusToString(CreditApplication.ApplicationStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default CreditApplication.ApplicationStatus stringToStatus(String status) {
        return status != null ? CreditApplication.ApplicationStatus.valueOf(status) : null;
    }

    @Named("localDateTimeToLocalDate")
    default LocalDate localDateTimeToLocalDate(LocalDateTime ldt) {
        return ldt != null ? ldt.toLocalDate() : null;
    }

    @Named("localDateToLocalDateTime")
    default LocalDateTime localDateToLocalDateTime(LocalDate d) {
        return d != null ? d.atStartOfDay() : null;
    }
}
