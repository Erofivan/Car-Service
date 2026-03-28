package com.erofivan.domain.cars.engines;

import com.erofivan.domain.cars.fuels.FuelType;

public record Engine(FuelType fuelType, EnginePower power, EngineVolume volume) {
    public static Engine of(FuelType fuelType, EnginePower power, EngineVolume volume) {
        return new Engine(fuelType, power, volume);
    }
}
