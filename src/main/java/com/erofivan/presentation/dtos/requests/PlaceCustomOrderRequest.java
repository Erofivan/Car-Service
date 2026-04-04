package com.erofivan.presentation.dtos.requests;

import java.util.UUID;

public record PlaceCustomOrderRequest(UUID clientId, String modelCode) {
}
