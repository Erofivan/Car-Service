package com.erofivan;

import com.erofivan.application.core.CarService;
import com.erofivan.application.core.ConfigurationService;
import com.erofivan.application.core.ManagerAssignmentService;
import com.erofivan.application.core.ModelDirectory;
import com.erofivan.application.core.OrderService;
import com.erofivan.application.core.PartService;
import com.erofivan.application.core.TestDriveService;
import com.erofivan.infrastructure.persistence.InMemoryPersistenceContext;
import com.erofivan.infrastructure.seeding.DataSeeder;
import com.erofivan.presentation.controllers.ClientController;
import com.erofivan.presentation.controllers.ManagerController;
import com.erofivan.presentation.controllers.SystemAdministratorController;
import com.erofivan.presentation.controllers.WarehouseAdministratorController;

public final class App {
    public static void main(String[] args) {
        var context = new InMemoryPersistenceContext();
        DataSeeder.SeedData seedData = DataSeeder.seed(context);

        var modelDirectory = new ModelDirectory();
        var managerAssignment = new ManagerAssignmentService();

        var carService = new CarService(context);
        var configurationService = new ConfigurationService(modelDirectory);
        var orderService = new OrderService(context, managerAssignment, modelDirectory);
        var partService = new PartService(context);
        var testDriveService = new TestDriveService(context);

        var clientController = new ClientController(
            carService,
            configurationService,
            orderService,
            testDriveService
        );
        var managerController = new ManagerController(context);
        var warehouseController = new WarehouseAdministratorController(context, partService);
        var systemAdministratorController = new SystemAdministratorController(carService);

        clientController.run(seedData.demoClientId(), seedData.demoCarId());
        managerController.run();
        warehouseController.run();
        systemAdministratorController.run(seedData.demoCarId());
    }
}
