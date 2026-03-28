package com.erofivan.domain.common.ids;

import java.util.UUID;

public record UserId(UUID value) {
    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId generate() {
        return of(UUID.randomUUID());
    }

    public static UserId from(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
