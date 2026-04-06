package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.CarCatalogService;
import com.erofivan.presentation.dtos.responses.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarRestController {
    private final CarCatalogService carCatalogService;

    @GetMapping("/{carId}")
    public CarResponse getCarById(@PathVariable UUID carId) {
        return carCatalogService.getCarById(carId);
    }

    @GetMapping
    public List<CarResponse> getCars(
        @RequestParam(required = false) String brandCode,
        @RequestParam(required = false) String modelCode,
        @RequestParam(required = false) String bodyType,
        @RequestParam(required = false) String fuelType,
        @RequestParam(required = false) String transmission,
        @RequestParam(required = false) String drivetrain,
        @RequestParam(required = false) String color,
        @RequestParam(required = false) Long minPrice,
        @RequestParam(required = false) Long maxPrice,
        @RequestParam(required = false) Integer minPower,
        @RequestParam(required = false) Integer maxPower,
        @RequestParam(required = false) Double minEngine,
        @RequestParam(required = false) Double maxEngine,
        @RequestParam(required = false) String componentName
    ) {
        return carCatalogService.getCars(
            brandCode,
            modelCode,
            bodyType,
            fuelType,
            transmission,
            drivetrain,
            color,
            minPrice,
            maxPrice,
            minPower,
            maxPower,
            minEngine,
            maxEngine,
            componentName
        );
    }
}
