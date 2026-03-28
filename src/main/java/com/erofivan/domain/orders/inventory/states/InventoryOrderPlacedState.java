package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public final class InventoryOrderPlacedState implements InventoryOrderState {
    @Override
    public boolean tryApproveByManager(InventoryOrderCore core) {
        core.updateState(new InventoryOrderApprovedByManagerState());
        return true;
    }

    @Override
    public boolean tryCancel(InventoryOrderCore core) {
        core.updateState(new InventoryOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "PLACED";
    }
}
