package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import com.erofivan.presentation.dtos.responses.CarResponse;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarResponse getCarById(UUID carId);

    List<CarResponse> getCars(
        String brandCode,
        String modelCode,
        String bodyType,
        String fuelType,
        String transmission,
        String drivetrain,
        String color,
        Long minPrice,
        Long maxPrice,
        Integer minPower,
        Integer maxPower,
        Double minEngine,
        Double maxEngine,
        String componentName
    );

    CarAvailabilityResponse getCarAvailability(UUID carId);
}
