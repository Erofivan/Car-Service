package com.erofivan.presentation.dtos.requests;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleTestDriveRequest(UUID clientId, UUID carId, LocalDateTime startsAt) {
}
