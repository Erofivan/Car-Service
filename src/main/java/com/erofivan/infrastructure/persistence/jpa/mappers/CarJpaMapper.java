package com.erofivan.infrastructure.persistence.jpa.mappers;

import com.erofivan.infrastructure.persistence.jpa.model.CarEntity;
import com.erofivan.presentation.dtos.responses.CarResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarJpaMapper {
    @Mapping(target = "brand", source = "model.brand.name")
    @Mapping(target = "brandCode", source = "model.brand.code")
    @Mapping(target = "model", source = "model.name")
    @Mapping(target = "modelCode", source = "model.code")
    CarResponse toResponse(CarEntity entity);
}
