package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.orders.custom.CustomOrder;

import java.util.List;
import java.util.Optional;

public interface CustomOrderRepository {
    void add(CustomOrder order);

    void update(CustomOrder order);

    Optional<CustomOrder> findById(OrderId id);

    List<CustomOrder> query(OrderQuery query);
}
