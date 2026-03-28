package com.erofivan.application.contracts.cars;

import com.erofivan.application.contracts.cars.operations.GetCar;
import com.erofivan.application.contracts.cars.operations.ListCars;

public interface CarServiceContract {
    ListCars.Response listCars(ListCars.Request request);

    GetCar.Response getCar(GetCar.Request request);
}
