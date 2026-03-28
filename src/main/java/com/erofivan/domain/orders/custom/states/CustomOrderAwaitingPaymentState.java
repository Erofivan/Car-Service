package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderAwaitingPaymentState implements CustomOrderState {
    @Override
    public boolean tryMarkPaid(CustomOrderCore core) {
        core.updateState(new CustomOrderPaidState());
        return true;
    }

    @Override
    public boolean tryCancel(CustomOrderCore core) {
        core.updateState(new CustomOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "AWAITING_PAYMENT";
    }
}
