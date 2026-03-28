package com.erofivan.application.contracts.testdrives;

import com.erofivan.application.contracts.testdrives.operations.ScheduleTestDrive;

public interface TestDriveServiceContract {
    ScheduleTestDrive.Response schedule(ScheduleTestDrive.Request request);
}
