package com.erofivan.presentation.dto;

import java.util.UUID;

public record PlaceCustomOrderRequest(UUID clientId, String modelCode) {
}
