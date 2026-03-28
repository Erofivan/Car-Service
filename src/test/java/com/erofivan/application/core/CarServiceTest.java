package com.erofivan.application.core;

import com.erofivan.application.contracts.cars.operations.GetCar;
import com.erofivan.infrastructure.persistence.InMemoryPersistenceContext;
import com.erofivan.infrastructure.seeding.DataSeeder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CarServiceTest {

    @Test
    void shouldReturnCarById() {
        // arrange
        var context = new InMemoryPersistenceContext();
        var seed = DataSeeder.seed(context);
        var service = new CarService(context);

        // act
        var response = service.getCar(new GetCar.Request(seed.demoCarId().toString()));

        // assert
        var success = assertInstanceOf(GetCar.Success.class, response);
        assertEquals(seed.demoCarId().toString(), success.car().id());
    }

    @Test
    void shouldReturnNotFoundForUnknownCarId() {
        // arrange
        var context = new InMemoryPersistenceContext();
        DataSeeder.seed(context);
        var service = new CarService(context);
        var unknownId = UUID.randomUUID().toString();

        // act
        var response = service.getCar(new GetCar.Request(unknownId));

        // assert
        assertInstanceOf(GetCar.NotFound.class, response);
    }
}
