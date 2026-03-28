package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.orders.inventory.InventoryOrder;

import java.util.List;
import java.util.Optional;

public interface InventoryOrderRepository {
    void add(InventoryOrder order);

    void update(InventoryOrder order);

    Optional<InventoryOrder> findById(OrderId id);

    List<InventoryOrder> query(OrderQuery query);
}
