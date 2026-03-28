package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public final class InventoryOrderPaidState implements InventoryOrderState {
    @Override
    public boolean tryMarkReadyForDelivery(InventoryOrderCore core) {
        core.updateState(new InventoryOrderReadyForDeliveryState());
        return true;
    }

    @Override
    public String statusName() {
        return "PAID";
    }
}
