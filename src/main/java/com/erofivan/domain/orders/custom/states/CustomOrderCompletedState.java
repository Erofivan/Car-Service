package com.erofivan.domain.orders.custom.states;

public final class CustomOrderCompletedState implements CustomOrderState {
    @Override
    public String statusName() {
        return "COMPLETED";
    }
}
