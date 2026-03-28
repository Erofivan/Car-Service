package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderAwaitingDeliveryState implements CustomOrderState {
    @Override
    public boolean tryMarkReadyForDelivery(CustomOrderCore core) {
        core.updateState(new CustomOrderReadyForDeliveryState());
        return true;
    }

    @Override
    public String statusName() {
        return "AWAITING_DELIVERY";
    }
}
