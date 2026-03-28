package com.erofivan.application.contracts.configurations.dtos;

import java.util.Map;

public record ConfigurationDto(String modelCode, Map<String, String> selectedOptions,
                               long basePrice, long totalSurcharge, long totalPrice) {
}
