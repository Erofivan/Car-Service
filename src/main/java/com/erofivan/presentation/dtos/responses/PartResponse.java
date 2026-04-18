package com.erofivan.presentation.dtos.responses;

import java.util.UUID;

public record PartResponse(
    UUID id,
    String name,
    String description,
    Long price
) {
}
