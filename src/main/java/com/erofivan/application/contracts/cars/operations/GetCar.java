package com.erofivan.application.contracts.cars.operations;

import com.erofivan.application.contracts.cars.dtos.CarDto;

public final class GetCar {
    private GetCar() {
    }

    public record Request(String carId) {
    }

    public sealed interface Response permits Success, NotFound {
    }

    public record Success(CarDto car) implements Response {
    }

    public record NotFound(String message) implements Response {
    }
}
