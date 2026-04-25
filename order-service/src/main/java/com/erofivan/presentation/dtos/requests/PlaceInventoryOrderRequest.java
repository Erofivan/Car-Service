package com.erofivan.presentation.dtos.requests;

import java.util.UUID;

public record PlaceInventoryOrderRequest(
    UUID carId
) {
}
