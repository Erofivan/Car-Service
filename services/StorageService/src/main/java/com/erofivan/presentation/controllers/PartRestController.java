package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.PartCatalogService;
import com.erofivan.presentation.dtos.responses.PartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'WAREHOUSE_ADMIN', 'ADMIN')")
public class PartRestController {
    private final PartCatalogService partCatalogService;

    @GetMapping
    public List<PartResponse> listParts() {
        return partCatalogService.listParts();
    }
}
