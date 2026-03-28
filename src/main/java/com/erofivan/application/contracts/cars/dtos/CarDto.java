package com.erofivan.application.contracts.cars.dtos;

public record CarDto(
        String id,
        String brand,
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
