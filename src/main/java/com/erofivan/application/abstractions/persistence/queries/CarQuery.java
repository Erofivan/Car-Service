package com.erofivan.application.abstractions.persistence.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class CarQuery {
    private final Long minPrice;
    private final Long maxPrice;
    private final String brandCode;
    private final String modelCode;
    private final String bodyType;
    private final String fuelType;
    private final Integer minPowerHp;
    private final Integer maxPowerHp;
    private final Double minEngineLitres;
    private final Double maxEngineLitres;
    private final String transmissionType;
    private final String drivetrainType;
    private final String color;
    private final Boolean onlyAvailable;
}
