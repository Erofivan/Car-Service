package com.erofivan.application.core;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.contracts.testdrives.TestDriveServiceContract;
import com.erofivan.application.contracts.testdrives.operations.ScheduleTestDrive;
import com.erofivan.application.core.mappings.TestDriveMappings;
import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.TestDriveRequestId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.testdrives.TestDriveRequest;
import com.erofivan.domain.testdrives.TestDriveSlot;

public final class TestDriveService implements TestDriveServiceContract {
    private final IPersistenceContext context;

    public TestDriveService(IPersistenceContext context) {
        this.context = context;
    }

    @Override
    public ScheduleTestDrive.Response schedule(ScheduleTestDrive.Request request) {
        try {
            UserId clientId = UserId.from(request.clientId());
            if (context.users().findClientById(clientId).isEmpty()) {
                return new ScheduleTestDrive.Failed("Client not found");
            }

            CarId carId = CarId.from(request.carId());
            var car = context.cars().findById(carId).orElse(null);
            if (car == null) {
                return new ScheduleTestDrive.Failed("Car not found");
            }
            if (!car.isTestDriveEnabled()) {
                return new ScheduleTestDrive.Failed("Test drive is disabled for this car");
            }

            TestDriveRequest testDriveRequest = TestDriveRequest.of(
                TestDriveRequestId.generate(),
                clientId,
                carId,
                TestDriveSlot.of(request.startsAt())
            );
            context.testDrives().add(testDriveRequest);

            return new ScheduleTestDrive.Success(TestDriveMappings.toDto(testDriveRequest));
        } catch (RuntimeException exception) {
            return new ScheduleTestDrive.Failed(exception.getMessage());
        }
    }
}
