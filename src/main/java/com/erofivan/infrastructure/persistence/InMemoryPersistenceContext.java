package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.abstractions.persistence.repositories.CarRepository;
import com.erofivan.application.abstractions.persistence.repositories.CustomOrderRepository;
import com.erofivan.application.abstractions.persistence.repositories.InventoryOrderRepository;
import com.erofivan.application.abstractions.persistence.repositories.PartRepository;
import com.erofivan.application.abstractions.persistence.repositories.TestDriveRepository;
import com.erofivan.application.abstractions.persistence.repositories.UserRepository;

public final class InMemoryPersistenceContext implements IPersistenceContext {
    private final CarRepository cars = new InMemoryCarRepository();
    private final PartRepository parts = new InMemoryPartRepository();
    private final InventoryOrderRepository inventoryOrders = new InMemoryInventoryOrderRepository();
    private final CustomOrderRepository customOrders = new InMemoryCustomOrderRepository();
    private final TestDriveRepository testDrives = new InMemoryTestDriveRepository();
    private final UserRepository users = new InMemoryUserRepository();

    @Override
    public CarRepository cars() {
        return cars;
    }

    @Override
    public PartRepository parts() {
        return parts;
    }

    @Override
    public InventoryOrderRepository inventoryOrders() {
        return inventoryOrders;
    }

    @Override
    public CustomOrderRepository customOrders() {
        return customOrders;
    }

    @Override
    public TestDriveRepository testDrives() {
        return testDrives;
    }

    @Override
    public UserRepository users() {
        return users;
    }
}
