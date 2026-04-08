package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.ConfigurationCatalogService;
import com.erofivan.presentation.dtos.requests.BuildConfigurationRequest;
import com.erofivan.presentation.dtos.responses.ConfigurationResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configurations")
@RequiredArgsConstructor
public class ConfigurationRestController {
    private final ConfigurationCatalogService configurationCatalogService;

    @GetMapping("/{modelCode}")
    public ConfigurationResponse getDefaultConfiguration(@PathVariable String modelCode) {
        return configurationCatalogService.getDefaultConfiguration(modelCode);
    }

    @PostMapping("/{modelCode}")
    public ConfigurationResponse buildConfiguration(
        @PathVariable String modelCode,
        @RequestBody @NonNull BuildConfigurationRequest request
    ) {
        return configurationCatalogService.buildConfiguration(modelCode, request.optionIds());
    }
}
