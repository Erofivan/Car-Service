package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public final class InventoryOrderReadyForDeliveryState implements InventoryOrderState {
    @Override
    public boolean tryComplete(InventoryOrderCore core) {
        core.updateState(new InventoryOrderCompletedState());
        return true;
    }

    @Override
    public String statusName() {
        return "READY_FOR_DELIVERY";
    }
}
