package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.TestDriveResponse;

import java.util.List;

public interface TestDriveService {
    List<TestDriveResponse> getTestDrives();

    TestDriveResponse schedule(ScheduleTestDriveRequest request);
}
