package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.domain.common.ids.TestDriveRequestId;
import com.erofivan.domain.testdrives.TestDriveRequest;

import java.util.List;
import java.util.Optional;

public interface TestDriveRepository {
    void add(TestDriveRequest request);

    void remove(TestDriveRequestId id);

    Optional<TestDriveRequest> findById(TestDriveRequestId id);

    List<TestDriveRequest> listAll();
}
