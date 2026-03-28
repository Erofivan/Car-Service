package com.erofivan.domain.common.ids;

import java.util.UUID;

public record TestDriveRequestId(UUID value) {
    public static TestDriveRequestId of(UUID value) {
        return new TestDriveRequestId(value);
    }

    public static TestDriveRequestId generate() {
        return of(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
