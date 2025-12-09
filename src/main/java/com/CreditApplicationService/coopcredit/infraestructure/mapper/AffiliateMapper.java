package com.CreditApplicationService.coopcredit.infraestructure.mapper;

import com.CreditApplicationService.coopcredit.domain.model.Affiliate;
import com.CreditApplicationService.coopcredit.infraestructure.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    AffiliateMapper INSTANCE = Mappers.getMapper(AffiliateMapper.class);

    public AffiliateEntity toEntity(Affiliate affiliate);
    public Affiliate toDomain(AffiliateEntity entity);
    // Si necesitas mapear enums entre String y enum, puedes agregar métodos default aquí
}
