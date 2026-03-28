package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderPaidState implements CustomOrderState {
    @Override
    public boolean tryMoveToAwaitingDelivery(CustomOrderCore core) {
        core.updateState(new CustomOrderAwaitingDeliveryState());
        return true;
    }

    @Override
    public String statusName() {
        return "PAID";
    }
}
