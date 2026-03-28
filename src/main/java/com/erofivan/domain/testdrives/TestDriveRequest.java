package com.erofivan.domain.testdrives;

import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.TestDriveRequestId;
import com.erofivan.domain.common.ids.UserId;

public record TestDriveRequest(TestDriveRequestId id, UserId clientId, CarId carId, TestDriveSlot slot) {
    public static TestDriveRequest of(TestDriveRequestId id, UserId clientId, CarId carId, TestDriveSlot slot) {
        return new TestDriveRequest(id, clientId, carId, slot);
    }
}
