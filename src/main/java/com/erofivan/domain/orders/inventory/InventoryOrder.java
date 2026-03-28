package com.erofivan.domain.orders.inventory;

import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.common.ids.UserId;

public final class InventoryOrder {
    private final InventoryOrderCore core;

    public InventoryOrder(InventoryOrderCore core) {
        this.core = core;
    }

    public OrderId id() {return core.getId();}

    public UserId clientId() {return core.getClientId();}

    public UserId managerId() {return core.getManagerId();}

    public CarId carId() {return core.getCarId();}

    public String status() {return core.status();}

    public boolean tryApproveByManager() {return core.getState().tryApproveByManager(core);}

    public boolean tryMoveToAwaitingPayment() {return core.getState().tryMoveToAwaitingPayment(core);}

    public boolean tryMarkPaid() {return core.getState().tryMarkPaid(core);}

    public boolean tryMarkReadyForDelivery() {return core.getState().tryMarkReadyForDelivery(core);}

    public boolean tryComplete() {return core.getState().tryComplete(core);}

    public boolean tryCancel() {return core.getState().tryCancel(core);}
}
