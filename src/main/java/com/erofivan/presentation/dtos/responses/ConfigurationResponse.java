package com.erofivan.presentation.dtos.responses;

import java.util.Map;

public record ConfigurationResponse(
    String modelCode,
    Map<String, String> selectedOptions,
    long basePrice,
    long totalSurcharge,
    long totalPrice
) {
}
