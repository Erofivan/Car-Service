package com.erofivan.presentation.dtos.responses;

import com.erofivan.presentation.dtos.ConfigurationOption;

import java.util.List;

public record ConfigurationResponse(
    String modelCode,
    List<ConfigurationOption> selectedOptions,
    Long basePrice,
    Long totalSurcharge,
    Long totalPrice
) {
}
