package com.erofivan.presentation.dto;

import java.util.UUID;

public record CustomOrderResponse(
    UUID id,
    UUID clientId,
    UUID managerId,
    String modelCode,
    String status,
    long totalPrice
) {
}
