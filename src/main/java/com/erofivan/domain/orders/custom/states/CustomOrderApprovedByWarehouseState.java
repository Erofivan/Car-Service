package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderApprovedByWarehouseState implements CustomOrderState {
    @Override
    public boolean tryMoveToAwaitingPayment(CustomOrderCore core) {
        core.updateState(new CustomOrderAwaitingPaymentState());
        return true;
    }

    @Override
    public boolean tryCancel(CustomOrderCore core) {
        core.updateState(new CustomOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "APPROVED_BY_WAREHOUSE";
    }
}
