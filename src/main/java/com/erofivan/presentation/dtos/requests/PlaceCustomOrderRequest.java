package com.erofivan.presentation.dtos.requests;

import java.util.List;
import java.util.UUID;

public record PlaceCustomOrderRequest(
    String modelCode,
    List<UUID> optionIds
) {
}
