package com.erofivan.domain.common;

public record Money(long value) {
    public static Money of(long value) {
        return new Money(value);
    }

    public static Money zero() {
        return of(0L);
    }

    public Money add(Money other) {
        return of(value + other.value);
    }

    public Money subtract(Money other) {
        return of(value - other.value);
    }
}
