package com.erofivan;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.application.contracts.cars.operations.ListCars;
import com.erofivan.application.core.CarService;
import com.erofivan.infrastructure.persistence.InMemoryPersistenceContext;
import com.erofivan.infrastructure.seeding.DataSeeder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AppTest {

    @Test
    void shouldListSeededCars() {
        // arrange
        var context = new InMemoryPersistenceContext();
        DataSeeder.seed(context);
        var service = new CarService(context);

        // act
        var response = service.listCars(new ListCars.Request(CarQuery.builder().build()));

        // assert
        var success = assertInstanceOf(ListCars.Success.class, response);
        assertEquals(5, success.cars().size());
    }
}
