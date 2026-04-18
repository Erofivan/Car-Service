package com.erofivan.presentation.dtos.responses;

import java.util.UUID;

public record CarResponse(
    UUID id,
    String brand,
    String brandCode,
    String model,
    String modelCode,
    String bodyType,
    String fuelType,
    Integer powerHp,
    Double engineLitres,
    String transmission,
    String drivetrain,
    String color,
    Long price,
    Boolean available,
    Boolean testDriveEnabled
) {
}
