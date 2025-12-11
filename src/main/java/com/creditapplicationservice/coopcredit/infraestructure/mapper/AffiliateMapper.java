package com.creditapplicationservice.coopcredit.infraestructure.mapper;

import com.creditapplicationservice.coopcredit.domain.model.Affiliate;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    AffiliateMapper INSTANCE = Mappers.getMapper(AffiliateMapper.class);

    public AffiliateEntity toEntity(Affiliate affiliate);
    public Affiliate toDomain(AffiliateEntity entity);
    // Si necesitas mapear enums entre String y enum, puedes agregar métodos default aquí
}
