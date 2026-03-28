package com.erofivan.presentation.dto;

import java.util.UUID;

public record PartResponse(
    UUID id,
    String name,
    String description,
    long price
) {
}
