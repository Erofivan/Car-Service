package com.erofivan.domain.common.ids;

import java.util.UUID;

public record CarId(UUID value) {
    public static CarId of(UUID value) {
        return new CarId(value);
    }

    public static CarId generate() {
        return of(UUID.randomUUID());
    }

    public static CarId from(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
