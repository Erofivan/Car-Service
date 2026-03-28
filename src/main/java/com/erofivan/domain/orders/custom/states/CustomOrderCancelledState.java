package com.erofivan.domain.orders.custom.states;

public final class CustomOrderCancelledState implements CustomOrderState {
    @Override
    public String statusName() {
        return "CANCELLED";
    }
}
