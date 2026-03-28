package com.erofivan.application.core;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.contracts.orders.OrderServiceContract;
import com.erofivan.application.contracts.orders.operations.PlaceCustomOrder;
import com.erofivan.application.contracts.orders.operations.PlaceInventoryOrder;
import com.erofivan.application.core.mappings.OrderMappings;
import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.configurations.CarConfiguration;
import com.erofivan.domain.configurations.CarConfigurator;
import com.erofivan.domain.orders.custom.CustomOrder;
import com.erofivan.domain.orders.custom.CustomOrderCore;
import com.erofivan.domain.orders.inventory.InventoryOrder;
import com.erofivan.domain.orders.inventory.InventoryOrderCore;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class OrderService implements OrderServiceContract {
    private final IPersistenceContext context;
    private final ManagerAssignmentService managerAssignmentService;
    private final ModelDirectory modelDirectory;

    @Override
    public PlaceInventoryOrder.Response placeInventoryOrder(PlaceInventoryOrder.Request request) {
        try {
            UserId clientId = UserId.from(request.clientId());
            if (context.users().findClientById(clientId).isEmpty()) {
                return new PlaceInventoryOrder.Failed("Client not found");
            }

            CarId carId = CarId.from(request.carId());
            var car = context.cars().findById(carId).orElse(null);
            if (car == null) {
                return new PlaceInventoryOrder.Failed("Car not found");
            }
            if (!car.isAvailable()) {
                return new PlaceInventoryOrder.Failed("Car is not available");
            }

            var order = new InventoryOrder(new InventoryOrderCore(
                OrderId.generate(),
                clientId,
                managerAssignmentService.assign(context),
                carId
            ));

            car.setAvailable(false);
            context.cars().update(car);
            context.inventoryOrders().add(order);

            return new PlaceInventoryOrder.Success(OrderMappings.toDto(order));
        } catch (RuntimeException exception) {
            return new PlaceInventoryOrder.Failed(exception.getMessage());
        }
    }

    @Override
    public PlaceCustomOrder.Response placeCustomOrder(PlaceCustomOrder.Request request) {
        try {
            UserId clientId = UserId.from(request.clientId());
            if (context.users().findClientById(clientId).isEmpty()) {
                return new PlaceCustomOrder.Failed("Client not found");
            }

            var model = modelDirectory.resolve(request.modelCode());
            CarConfigurator configurator = new CarConfigurator(model.spec());
            request.selectedOptions().forEach(configurator::select);
            CarConfiguration configuration = configurator.build();

            var order = new CustomOrder(new CustomOrderCore(
                OrderId.generate(),
                clientId,
                managerAssignmentService.assign(context),
                model.code(),
                configuration
            ));
            context.customOrders().add(order);
            long totalPrice = model.spec().basePrice().value() + configuration.totalSurcharge().value();
            return new PlaceCustomOrder.Success(OrderMappings.toDto(order, totalPrice));
        } catch (RuntimeException exception) {
            return new PlaceCustomOrder.Failed(exception.getMessage());
        }
    }
}
