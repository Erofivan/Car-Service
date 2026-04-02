package com.erofivan.presentation.controllers;

import com.erofivan.application.core.TestDriveCatalogService;
import com.erofivan.presentation.dto.ScheduleTestDriveRequest;
import com.erofivan.presentation.dto.TestDriveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-drives")
@RequiredArgsConstructor
public class TestDriveRestController {
    private final TestDriveCatalogService testDriveCatalogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestDriveResponse schedule(@RequestBody ScheduleTestDriveRequest request) {
        return testDriveCatalogService.schedule(request);
    }
}
