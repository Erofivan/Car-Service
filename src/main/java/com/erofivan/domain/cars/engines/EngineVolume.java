package com.erofivan.domain.cars.engines;

public record EngineVolume(double litres) {
    public static EngineVolume of(double litres) {
        return new EngineVolume(litres);
    }
}
