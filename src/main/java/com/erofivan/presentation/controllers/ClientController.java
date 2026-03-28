package com.erofivan.presentation.controllers;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.application.contracts.cars.CarServiceContract;
import com.erofivan.application.contracts.cars.operations.ListCars;
import com.erofivan.application.contracts.configurations.ConfigurationServiceContract;
import com.erofivan.application.contracts.configurations.operations.BuildConfiguration;
import com.erofivan.application.contracts.orders.OrderServiceContract;
import com.erofivan.application.contracts.orders.operations.PlaceCustomOrder;
import com.erofivan.application.contracts.orders.operations.PlaceInventoryOrder;
import com.erofivan.application.contracts.testdrives.TestDriveServiceContract;
import com.erofivan.application.contracts.testdrives.operations.ScheduleTestDrive;
import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.UserId;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ClientController {
    private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());

    private final CarServiceContract carService;
    private final ConfigurationServiceContract configurationService;
    private final OrderServiceContract orderService;
    private final TestDriveServiceContract testDriveService;

    public ClientController(
            CarServiceContract carService,
            ConfigurationServiceContract configurationService,
            OrderServiceContract orderService,
            TestDriveServiceContract testDriveService
    ) {
        this.carService = carService;
        this.configurationService = configurationService;
        this.orderService = orderService;
        this.testDriveService = testDriveService;
    }

    public void run(UserId clientId, CarId demoCarId) {
        LOGGER.info("Client: list cars");
        var listResponse = carService.listCars(new ListCars.Request(CarQuery.builder().build()));
        if (listResponse instanceof ListCars.Success success) {
            success.cars().forEach(car -> LOGGER.info("- " + car.brand() + " " + car.model() + " " + car.price()));
        }

        LOGGER.info("Client: build default configuration for BMW-320I");
        var configurationResponse = configurationService.buildConfiguration(new BuildConfiguration.Request("BMW-320I"));
        if (configurationResponse instanceof BuildConfiguration.Success success) {
            LOGGER.info("Configuration total: " + success.configuration().totalPrice());
        }

        LOGGER.info("Client: place inventory order");
        var inventoryResponse = orderService.placeInventoryOrder(
                new PlaceInventoryOrder.Request(clientId.toString(), demoCarId.toString())
        );
        if (inventoryResponse instanceof PlaceInventoryOrder.Success success) {
            LOGGER.info("Inventory order placed: " + success.order().id());
        }

        LOGGER.info("Client: place custom order");
        Map<String, String> options = new LinkedHashMap<>();
        options.put("Wheels", "18'' Aero");
        options.put("Transmission", "Automatic 8AT");
        options.put("Steering wheel", "M-Sport heated");
        options.put("Interior", "Leather Dakota");
        var customResponse = orderService.placeCustomOrder(
                new PlaceCustomOrder.Request(clientId.toString(), "BMW-320I", options)
        );
        if (customResponse instanceof PlaceCustomOrder.Success success) {
            LOGGER.info("Custom order placed: " + success.order().id() + " total=" + success.order().totalPrice());
        }

        LOGGER.info("Client: schedule test drive");
        var testDriveResponse = testDriveService.schedule(new ScheduleTestDrive.Request(
                clientId.toString(),
                demoCarId.toString(),
                LocalDateTime.now().plusDays(1)
        ));
        if (testDriveResponse instanceof ScheduleTestDrive.Success success) {
            LOGGER.info("Test drive request created: " + success.request().id());
        }
    }
}
