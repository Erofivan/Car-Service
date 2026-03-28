package com.erofivan.domain.orders.inventory.states;

public final class InventoryOrderCompletedState implements InventoryOrderState {
    @Override
    public String statusName() {
        return "COMPLETED";
    }
}
