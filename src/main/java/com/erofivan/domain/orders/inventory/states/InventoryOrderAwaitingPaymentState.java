package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public final class InventoryOrderAwaitingPaymentState implements InventoryOrderState {
    @Override
    public boolean tryMarkPaid(InventoryOrderCore core) {
        core.updateState(new InventoryOrderPaidState());
        return true;
    }

    @Override
    public boolean tryCancel(InventoryOrderCore core) {
        core.updateState(new InventoryOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "AWAITING_PAYMENT";
    }
}
