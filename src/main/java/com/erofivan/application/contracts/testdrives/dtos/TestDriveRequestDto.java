package com.erofivan.application.contracts.testdrives.dtos;

import java.time.LocalDateTime;

public record TestDriveRequestDto(String id, String clientId, String carId,
                                  LocalDateTime startsAt) {
}
