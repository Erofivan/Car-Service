package com.erofivan.domain.cars.engines;

public record EnginePower(int horsePower) {
    public static EnginePower of(int horsePower) {
        return new EnginePower(horsePower);
    }
}
