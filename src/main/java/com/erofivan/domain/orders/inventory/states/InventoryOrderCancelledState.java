package com.erofivan.domain.orders.inventory.states;

public final class InventoryOrderCancelledState implements InventoryOrderState {
    @Override
    public String statusName() {
        return "CANCELLED";
    }
}
