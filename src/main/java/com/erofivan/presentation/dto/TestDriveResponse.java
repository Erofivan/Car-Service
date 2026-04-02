package com.erofivan.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDriveResponse(
    UUID id,
    UUID clientId,
    UUID carId,
    LocalDateTime startsAt
) {
}
