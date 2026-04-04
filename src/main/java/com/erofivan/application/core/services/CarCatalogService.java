package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.infrastructure.persistence.jpa.mappers.CarMapper;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.specifications.CarJpaSpecifications;
import com.erofivan.presentation.dtos.responses.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarCatalogService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarResponse getCarById(UUID carId) {
        CarEntity car = carRepository.findById(carId)
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", carId.toString()));
        return carMapper.toResponse(car);
    }

    public List<CarResponse> getCars(
        String brandCode, String modelCode, String bodyType,
        String fuelType, String transmission, String drivetrain,
        String color, Long minPrice, Long maxPrice,
        Integer minPower, Integer maxPower,
        Double minEngine, Double maxEngine,
        String componentName
    ) {
        return carRepository.findAll(
                CarJpaSpecifications.byFilters(
                    brandCode, modelCode, bodyType, fuelType,
                    transmission, drivetrain, color,
                    minPrice, maxPrice, minPower, maxPower,
                    minEngine, maxEngine, componentName)
            )
            .stream()
            .map(carMapper::toResponse)
            .toList();
    }
}
