package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.application.abstractions.persistence.repositories.CustomOrderRepository;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.orders.custom.CustomOrder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryCustomOrderRepository implements CustomOrderRepository {
    private final Map<OrderId, CustomOrder> storage = new LinkedHashMap<>();

    @Override
    public void add(CustomOrder order) {
        storage.put(order.id(), order);
    }

    @Override
    public void update(CustomOrder order) {
        storage.put(order.id(), order);
    }

    @Override
    public Optional<CustomOrder> findById(OrderId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<CustomOrder> query(OrderQuery query) {
        return storage.values().stream()
            .filter(order -> query.getClientId() == null || order.clientId().toString().equals(query.getClientId()))
            .filter(order -> query.getManagerId() == null || order.managerId().toString().equals(query.getManagerId()))
            .filter(order -> query.getStatus() == null || order.status().equalsIgnoreCase(query.getStatus()))
            .toList();
    }
}
