package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.application.abstractions.persistence.repositories.InventoryOrderRepository;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.orders.inventory.InventoryOrder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryInventoryOrderRepository implements InventoryOrderRepository {
    private final Map<OrderId, InventoryOrder> storage = new LinkedHashMap<>();

    @Override
    public void add(InventoryOrder order) {
        storage.put(order.id(), order);
    }

    @Override
    public void update(InventoryOrder order) {
        storage.put(order.id(), order);
    }

    @Override
    public Optional<InventoryOrder> findById(OrderId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<InventoryOrder> query(OrderQuery query) {
        return storage.values().stream()
            .filter(order -> query.getClientId() == null || order.clientId().toString().equals(query.getClientId()))
            .filter(order -> query.getManagerId() == null || order.managerId().toString().equals(query.getManagerId()))
            .filter(order -> query.getStatus() == null || order.status().equalsIgnoreCase(query.getStatus()))
            .toList();
    }
}
