package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.AvailableCarCatalogService;
import com.erofivan.presentation.dtos.responses.AvailableCarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class AvailableCarRestController {
    private final AvailableCarCatalogService availableCarCatalogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<AvailableCarResponse> getAvailableCars() {
        return availableCarCatalogService.getAvailableCars();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public AvailableCarResponse getAvailableCarById(@PathVariable UUID id) {
        return availableCarCatalogService.getAvailableCarById(id);
    }
}
