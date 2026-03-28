package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public final class CustomOrderPlacedState implements CustomOrderState {
    @Override
    public boolean tryApproveByWarehouse(CustomOrderCore core) {
        core.updateState(new CustomOrderApprovedByWarehouseState());
        return true;
    }

    @Override
    public boolean tryCancel(CustomOrderCore core) {
        core.updateState(new CustomOrderCancelledState());
        return true;
    }

    @Override
    public String statusName() {
        return "PLACED";
    }
}
