package com.erofivan.presentation.dtos.responses;

import com.erofivan.domain.OrderStatus;

import java.util.UUID;

public record CustomOrderResponse(
    UUID id,
    UUID clientId,
    UUID managerId,
    String modelCode,
    OrderStatus status,
    Long totalPrice
) {
}
