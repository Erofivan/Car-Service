package com.erofivan.presentation.dto;

import java.util.Map;

public record ConfigurationResponse(
    String modelCode,
    Map<String, String> selectedOptions,
    long basePrice,
    long totalSurcharge,
    long totalPrice
) {
}
