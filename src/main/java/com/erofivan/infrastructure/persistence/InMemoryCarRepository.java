package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.queries.CarQuery;
import com.erofivan.application.abstractions.persistence.repositories.CarRepository;
import com.erofivan.domain.cars.Car;
import com.erofivan.domain.common.ids.CarId;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class InMemoryCarRepository implements CarRepository {
    private final Map<CarId, Car> storage = new LinkedHashMap<>();

    @Override
    public void add(Car car) {
        storage.put(car.getId(), car);
    }

    @Override
    public void update(Car car) {
        storage.put(car.getId(), car);
    }

    @Override
    public void remove(CarId id) {
        storage.remove(id);
    }

    @Override
    public Optional<Car> findById(CarId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Car> query(CarQuery query) {
        Predicate<Car> predicate = car ->
            (query.getMinPrice() == null || car.getPrice().value() >= query.getMinPrice())
                && (query.getMaxPrice() == null || car.getPrice().value() <= query.getMaxPrice())
                && (query.getBrandCode() == null || car.brandCode().equals(query.getBrandCode()))
                && (query.getModelCode() == null || car.modelCode().equals(query.getModelCode()))
                && (query.getBodyType() == null || car.getVisualSpec().bodyType().displayName().equalsIgnoreCase(query.getBodyType()))
                && (query.getFuelType() == null || car.getStaticSpec().engine().fuelType().displayName().equalsIgnoreCase(query.getFuelType()))
                && (query.getMinPowerHp() == null || car.getStaticSpec().engine().power().horsePower() >= query.getMinPowerHp())
                && (query.getMaxPowerHp() == null || car.getStaticSpec().engine().power().horsePower() <= query.getMaxPowerHp())
                && (query.getMinEngineLitres() == null || car.getStaticSpec().engine().volume().litres() >= query.getMinEngineLitres())
                && (query.getMaxEngineLitres() == null || car.getStaticSpec().engine().volume().litres() <= query.getMaxEngineLitres())
                && (query.getTransmissionType() == null || car.getStaticSpec().transmissionType().displayName().equalsIgnoreCase(query.getTransmissionType()))
                && (query.getDrivetrainType() == null || car.getStaticSpec().drivetrainType().displayName().equalsIgnoreCase(query.getDrivetrainType()))
                && (query.getColor() == null || car.getVisualSpec().color().displayName().equalsIgnoreCase(query.getColor()))
                && (!Boolean.TRUE.equals(query.getOnlyAvailable()) || car.isAvailable());

        return storage.values().stream().filter(predicate).toList();
    }
}
