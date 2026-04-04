package com.erofivan.presentation.dtos.responses;

import com.erofivan.domain.OrderStatus;

import java.util.UUID;

public record InventoryOrderResponse(
    UUID id,
    UUID clientId,
    UUID managerId,
    UUID carId,
    OrderStatus status
) {
}
