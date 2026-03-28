package com.erofivan.presentation.controllers;

import com.erofivan.application.core.CarCatalogService;
import com.erofivan.presentation.dto.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarRestController {
    private final CarCatalogService carCatalogService;

    @GetMapping
    public List<CarResponse> getCars(
        @RequestParam(required = false) String brandCode,
        @RequestParam(required = false) String modelCode,
        @RequestParam(required = false) String componentName
    ) {
        return carCatalogService.getCars(brandCode, modelCode, componentName);
    }
}
