package com.erofivan.application.contracts.testdrives.operations;

import com.erofivan.application.contracts.testdrives.dtos.TestDriveRequestDto;

import java.time.LocalDateTime;

public final class ScheduleTestDrive {
    private ScheduleTestDrive() {
    }

    public record Request(String clientId, String carId, LocalDateTime startsAt) {
    }

    public sealed interface Response permits Success, Failed {
    }

    public record Success(TestDriveRequestDto request) implements Response {
    }

    public record Failed(String message) implements Response {
    }
}
