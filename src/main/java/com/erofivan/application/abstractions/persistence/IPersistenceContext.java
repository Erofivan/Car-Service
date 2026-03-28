package com.erofivan.application.abstractions.persistence;

import com.erofivan.application.abstractions.persistence.repositories.CarRepository;
import com.erofivan.application.abstractions.persistence.repositories.CustomOrderRepository;
import com.erofivan.application.abstractions.persistence.repositories.InventoryOrderRepository;
import com.erofivan.application.abstractions.persistence.repositories.PartRepository;
import com.erofivan.application.abstractions.persistence.repositories.TestDriveRepository;
import com.erofivan.application.abstractions.persistence.repositories.UserRepository;

public interface IPersistenceContext {
    CarRepository cars();

    PartRepository parts();

    InventoryOrderRepository inventoryOrders();

    CustomOrderRepository customOrders();

    TestDriveRepository testDrives();

    UserRepository users();
}
