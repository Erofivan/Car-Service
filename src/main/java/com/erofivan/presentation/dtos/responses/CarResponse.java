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
    int powerHp,
    double engineLitres,
    String transmission,
    String drivetrain,
    String color,
    long price,
    boolean available,
    boolean testDriveEnabled
) {
}
