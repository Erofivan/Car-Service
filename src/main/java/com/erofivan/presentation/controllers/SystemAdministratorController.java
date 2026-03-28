package com.erofivan.presentation.controllers;

import com.erofivan.application.contracts.cars.CarServiceContract;
import com.erofivan.application.contracts.cars.operations.GetCar;
import com.erofivan.domain.common.ids.CarId;

import java.util.logging.Logger;

public final class SystemAdministratorController {
    private static final Logger LOGGER = Logger.getLogger(SystemAdministratorController.class.getName());

    private final CarServiceContract carService;

    public SystemAdministratorController(CarServiceContract carService) {
        this.carService = carService;
    }

    public void run(CarId demoCarId) {
        LOGGER.info("System admin: inspect car");
        var response = carService.getCar(new GetCar.Request(demoCarId.toString()));
        if (response instanceof GetCar.Success success) {
            LOGGER.info("- car: " + success.car().brand() + " " + success.car().model() + " available=" + success.car().available());
        }
    }
}
