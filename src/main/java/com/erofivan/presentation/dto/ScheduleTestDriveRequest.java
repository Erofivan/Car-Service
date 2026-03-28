package com.erofivan.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleTestDriveRequest(UUID clientId, UUID carId, LocalDateTime startsAt) {
}
