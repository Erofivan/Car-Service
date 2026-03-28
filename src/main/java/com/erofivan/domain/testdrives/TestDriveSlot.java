package com.erofivan.domain.testdrives;

import java.time.LocalDateTime;

public record TestDriveSlot(LocalDateTime startsAt) {
    public static TestDriveSlot of(LocalDateTime startsAt) {
        return new TestDriveSlot(startsAt);
    }
}
