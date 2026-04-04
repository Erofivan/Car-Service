package com.erofivan.presentation.dtos.responses;

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
