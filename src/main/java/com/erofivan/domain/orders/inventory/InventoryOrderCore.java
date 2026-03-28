package com.erofivan.domain.orders.inventory;

import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.orders.inventory.states.InventoryOrderPlacedState;
import com.erofivan.domain.orders.inventory.states.InventoryOrderState;
import lombok.Getter;

@Getter
public final class InventoryOrderCore {
    private final OrderId id;
    private final UserId clientId;
    private final UserId managerId;
    private final CarId carId;
    private InventoryOrderState state;

    public InventoryOrderCore(OrderId id, UserId clientId, UserId managerId, CarId carId) {
        this.id = id;
        this.clientId = clientId;
        this.managerId = managerId;
        this.carId = carId;
        this.state = new InventoryOrderPlacedState();
    }

    public void updateState(InventoryOrderState state) {
        this.state = state;
    }

    public String status() {
        return state.statusName();
    }
}
