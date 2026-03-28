package com.erofivan.application.core.mappings;

import com.erofivan.application.contracts.configurations.dtos.ConfigurationDto;
import com.erofivan.domain.configurations.CarConfiguration;
import com.erofivan.domain.configurations.CarModelSpec;

import java.util.Map;
import java.util.stream.Collectors;

public final class ConfigurationMappings {
    private ConfigurationMappings() {
    }

    public static ConfigurationDto toDto(CarModelSpec spec, CarConfiguration configuration) {
        Map<String, String> selectedOptions = configuration.selectedOptions().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().name()));
        long basePrice = spec.basePrice().value();
        long surcharge = configuration.totalSurcharge().value();

        return new ConfigurationDto(
            configuration.modelCode(),
            selectedOptions,
            basePrice,
            surcharge,
            basePrice + surcharge
        );
    }
}
