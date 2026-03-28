package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.repositories.TestDriveRepository;
import com.erofivan.domain.common.ids.TestDriveRequestId;
import com.erofivan.domain.testdrives.TestDriveRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryTestDriveRepository implements TestDriveRepository {
    private final Map<TestDriveRequestId, TestDriveRequest> storage = new LinkedHashMap<>();

    @Override
    public void add(TestDriveRequest request) {
        storage.put(request.id(), request);
    }

    @Override
    public void remove(TestDriveRequestId id) {
        storage.remove(id);
    }

    @Override
    public Optional<TestDriveRequest> findById(TestDriveRequestId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<TestDriveRequest> listAll() {
        return new ArrayList<>(storage.values());
    }
}
