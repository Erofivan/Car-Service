package com.erofivan.presentation.dtos.responses;

import java.util.UUID;

public record CarAvailabilityResponse(UUID id, boolean available, boolean testDriveEnabled) {
}
