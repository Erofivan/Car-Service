package com.erofivan.domain.common.ids;

import java.util.UUID;

public record PartId(UUID value) {
    public static PartId of(UUID value) {
        return new PartId(value);
    }

    public static PartId generate() {
        return of(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
