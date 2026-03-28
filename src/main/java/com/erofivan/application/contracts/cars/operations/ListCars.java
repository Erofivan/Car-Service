package com.erofivan.application.contracts.cars.operations;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.application.contracts.cars.dtos.CarDto;

import java.util.List;

public final class ListCars {
    private ListCars() {
    }

    public record Request(CarQuery query) {
    }

    public sealed interface Response permits Success {
    }

    public record Success(List<CarDto> cars) implements Response {
    }
}
