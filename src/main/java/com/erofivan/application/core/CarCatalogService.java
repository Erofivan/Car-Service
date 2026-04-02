package com.erofivan.application.core;

import com.erofivan.infrastructure.persistence.jpa.mappers.CarJpaMapper;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.specifications.CarJpaSpecifications;
import com.erofivan.presentation.dto.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarCatalogService {
    private final CarJpaRepository carJpaRepository;
    private final CarJpaMapper carJpaMapper;

    public List<CarResponse> getCars(String brandCode, String modelCode, String componentName) {
        return carJpaRepository.findAll(
                CarJpaSpecifications.byFilters(brandCode, modelCode, componentName)
            )
            .stream()
            .map(carJpaMapper::toResponse)
            .toList();
    }
}
