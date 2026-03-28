package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderReadyForDeliveryState implements CustomOrderState {
    @Override
    public boolean tryComplete(CustomOrderCore core) {
        core.updateState(new CustomOrderCompletedState());
        return true;
    }

    @Override
    public String statusName() {
        return "READY_FOR_DELIVERY";
    }
}
