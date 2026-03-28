package com.erofivan.application.core;

import com.erofivan.application.contracts.orders.operations.PlaceInventoryOrder;
import com.erofivan.infrastructure.persistence.InMemoryPersistenceContext;
import com.erofivan.infrastructure.seeding.DataSeeder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    @Test
    void shouldPlaceInventoryOrderAndReserveCar() {
    // arrange
        var context = new InMemoryPersistenceContext();
        var seed = DataSeeder.seed(context);
        var service = new OrderService(context, new ManagerAssignmentService(), new ModelDirectory());

    // act
        var response = service.placeInventoryOrder(new PlaceInventoryOrder.Request(
                seed.demoClientId().toString(),
                seed.demoCarId().toString()
        ));

    // assert
        assertInstanceOf(PlaceInventoryOrder.Success.class, response);
        var car = context.cars().findById(seed.demoCarId());
        assertTrue(car.isPresent());
        assertFalse(car.orElseThrow().isAvailable());
    }

    @Test
    void shouldReturnFailedWhenClientNotFound() {
    // arrange
        var context = new InMemoryPersistenceContext();
        var seed = DataSeeder.seed(context);
        var service = new OrderService(context, new ManagerAssignmentService(), new ModelDirectory());

    // act
        var response = service.placeInventoryOrder(new PlaceInventoryOrder.Request(
                UUID.randomUUID().toString(),
                seed.demoCarId().toString()
        ));

    // assert
        assertInstanceOf(PlaceInventoryOrder.Failed.class, response);
    }

    @Test
    void shouldReturnFailedWhenCarIsAlreadyReserved() {
    // arrange
        var context = new InMemoryPersistenceContext();
        var seed = DataSeeder.seed(context);
        var service = new OrderService(context, new ManagerAssignmentService(), new ModelDirectory());
        service.placeInventoryOrder(new PlaceInventoryOrder.Request(
                seed.demoClientId().toString(),
                seed.demoCarId().toString()
        ));

    // act
        var response = service.placeInventoryOrder(new PlaceInventoryOrder.Request(
                seed.demoClientId().toString(),
                seed.demoCarId().toString()
        ));

    // assert
        var failed = assertInstanceOf(PlaceInventoryOrder.Failed.class, response);
        assertTrue(failed.message().contains("not available"));
    }
}
