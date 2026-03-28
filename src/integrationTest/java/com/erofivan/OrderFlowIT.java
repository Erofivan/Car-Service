package com.erofivan;

import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.application.contracts.orders.operations.PlaceInventoryOrder;
import com.erofivan.application.core.ManagerAssignmentService;
import com.erofivan.application.core.ModelDirectory;
import com.erofivan.application.core.OrderService;
import com.erofivan.infrastructure.persistence.InMemoryPersistenceContext;
import com.erofivan.infrastructure.seeding.DataSeeder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderFlowIT {

    @Test
    void shouldPlaceInventoryOrderThroughRealContext() {
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
        assertEquals(1, context.inventoryOrders().query(OrderQuery.builder().build()).size());
        var car = context.cars().findById(seed.demoCarId());
        assertTrue(car.isPresent());
        assertFalse(car.orElseThrow().isAvailable());
    }
}
