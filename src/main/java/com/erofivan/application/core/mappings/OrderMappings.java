package com.erofivan.application.core.mappings;

import com.erofivan.application.contracts.orders.dtos.CustomOrderDto;
import com.erofivan.application.contracts.orders.dtos.InventoryOrderDto;
import com.erofivan.domain.orders.custom.CustomOrder;
import com.erofivan.domain.orders.inventory.InventoryOrder;

public final class OrderMappings {
    private OrderMappings() {
    }

    public static InventoryOrderDto toDto(InventoryOrder order) {
        return new InventoryOrderDto(
                order.id().toString(),
                order.clientId().toString(),
                order.managerId().toString(),
                order.carId().toString(),
                order.status()
        );
    }

    public static CustomOrderDto toDto(CustomOrder order) {
        return toDto(order, order.configuration().totalSurcharge().value());
    }

    public static CustomOrderDto toDto(CustomOrder order, long totalPrice) {
        return new CustomOrderDto(
                order.id().toString(),
                order.clientId().toString(),
                order.managerId().toString(),
                order.modelCode(),
                order.status(),
                totalPrice
        );
    }
}
