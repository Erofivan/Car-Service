package com.erofivan.presentation.dto;

import java.util.UUID;

public record PlaceInventoryOrderRequest(UUID clientId, UUID carId) {
}
