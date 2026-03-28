package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public final class InventoryOrderApprovedByManagerState implements InventoryOrderState {
    @Override
    public boolean tryMoveToAwaitingPayment(InventoryOrderCore core) {
        core.updateState(new InventoryOrderAwaitingPaymentState());
        return true;
    }

    @Override
    public boolean tryCancel(InventoryOrderCore core) {
        core.updateState(new InventoryOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "APPROVED_BY_MANAGER";
    }
}
