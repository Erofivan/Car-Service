package com.erofivan.application.core.mappings;

import com.erofivan.application.contracts.testdrives.dtos.TestDriveRequestDto;
import com.erofivan.domain.testdrives.TestDriveRequest;

public final class TestDriveMappings {
    private TestDriveMappings() {
    }

    public static TestDriveRequestDto toDto(TestDriveRequest request) {
        return new TestDriveRequestDto(
            request.id().toString(),
            request.clientId().toString(),
            request.carId().toString(),
            request.slot().startsAt()
        );
    }
}
