package com.erofivan.application.core.mappings;

import com.erofivan.application.contracts.cars.dtos.CarDto;
import com.erofivan.domain.cars.Car;

public final class CarMappings {
    private CarMappings() {
    }

    public static CarDto toDto(Car car) {
        return new CarDto(
                car.getId().toString(),
                car.getModel().brand().displayName(),
                car.getModel().name(),
                car.getModel().code(),
            car.getVisualSpec().bodyType().displayName(),
            car.getStaticSpec().engine().fuelType().displayName(),
            car.getStaticSpec().engine().power().horsePower(),
            car.getStaticSpec().engine().volume().litres(),
            car.getStaticSpec().transmissionType().displayName(),
            car.getStaticSpec().drivetrainType().displayName(),
            car.getVisualSpec().color().displayName(),
            car.getPrice().value(),
                car.isAvailable(),
                car.isTestDriveEnabled()
        );
    }
}
