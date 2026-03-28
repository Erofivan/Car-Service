package com.erofivan.domain.common.ids;

import java.util.UUID;

public record OrderId(UUID value) {
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    public static OrderId generate() {
        return of(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
