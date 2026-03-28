package com.erofivan.application.core;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.contracts.cars.CarServiceContract;
import com.erofivan.application.contracts.cars.operations.GetCar;
import com.erofivan.application.contracts.cars.operations.ListCars;
import com.erofivan.application.core.mappings.CarMappings;
import com.erofivan.domain.common.ids.CarId;

public final class CarService implements CarServiceContract {
    private final IPersistenceContext context;

    public CarService(IPersistenceContext context) {
        this.context = context;
    }

    @Override
    public ListCars.Response listCars(ListCars.Request request) {
        return new ListCars.Success(context.cars().query(request.query()).stream().map(CarMappings::toDto).toList());
    }

    @Override
    public GetCar.Response getCar(GetCar.Request request) {
        return context.cars().findById(CarId.from(request.carId()))
            .<GetCar.Response>map(car -> new GetCar.Success(CarMappings.toDto(car)))
            .orElseGet(() -> new GetCar.NotFound("Car not found"));
    }
}
