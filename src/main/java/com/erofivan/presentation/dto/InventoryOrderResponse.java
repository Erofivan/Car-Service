package com.erofivan.presentation.dto;

import java.util.UUID;

public record InventoryOrderResponse(
    UUID id,
    UUID clientId,
    UUID managerId,
    UUID carId,
    String status
) {
}
