package com.erofivan.presentation.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDriveResponse(
    UUID id,
    UUID clientId,
    UUID carId,
    LocalDateTime startsAt
) {
}
