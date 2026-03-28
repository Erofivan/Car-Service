package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.infrastructure.seeding.DataSeeder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryCarRepositoryTest {

    @Test
    void shouldFilterCarsByBrandCode() {
        // arrange
        var context = new InMemoryPersistenceContext();
        DataSeeder.seed(context);

        // act
        var cars = context.cars().query(CarQuery.builder().brandCode("BMW").build());

        // assert
        assertEquals(3, cars.size());
    }

    @Test
    void shouldReturnOnlyAvailableCarsWhenRequested() {
        // arrange
        var context = new InMemoryPersistenceContext();
        var seed = DataSeeder.seed(context);
        var car = context.cars().findById(seed.demoCarId()).orElseThrow();
        car.setAvailable(false);
        context.cars().update(car);

        // act
        var cars = context.cars().query(CarQuery.builder().onlyAvailable(true).build());

        // assert
        assertEquals(4, cars.size());
    }
}
