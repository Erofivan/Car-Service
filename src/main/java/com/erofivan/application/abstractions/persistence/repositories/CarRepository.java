package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.domain.cars.Car;
import com.erofivan.domain.common.ids.CarId;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    void add(Car car);

    void update(Car car);

    void remove(CarId id);

    Optional<Car> findById(CarId id);

    List<Car> query(CarQuery query);
}
