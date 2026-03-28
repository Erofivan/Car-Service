package com.erofivan.domain.orders.custom;

import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.configurations.CarConfiguration;
import com.erofivan.domain.orders.custom.states.CustomOrderPlacedState;
import com.erofivan.domain.orders.custom.states.CustomOrderState;
import lombok.Getter;

@Getter
public final class CustomOrderCore {
    private final OrderId id;
    private final UserId clientId;
    private final UserId managerId;
    private final String modelCode;
    private final CarConfiguration configuration;
    private CustomOrderState state;

    public CustomOrderCore(OrderId id, UserId clientId, UserId managerId, String modelCode, CarConfiguration configuration) {
        this.id = id;
        this.clientId = clientId;
        this.managerId = managerId;
        this.modelCode = modelCode;
        this.configuration = configuration;
        this.state = new CustomOrderPlacedState();
    }

    public void updateState(CustomOrderState state) {
        this.state = state;
    }

    public String status() {
        return state.statusName();
    }
}
