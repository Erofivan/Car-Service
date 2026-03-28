package com.erofivan.domain.orders.custom;

import com.erofivan.domain.common.ids.OrderId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.configurations.CarConfiguration;

public final class CustomOrder {
    private final CustomOrderCore core;

    public CustomOrder(CustomOrderCore core) {
        this.core = core;
    }

    public OrderId id() {return core.getId();}

    public UserId clientId() {return core.getClientId();}

    public UserId managerId() {return core.getManagerId();}

    public String modelCode() {return core.getModelCode();}

    public CarConfiguration configuration() {return core.getConfiguration();}

    public String status() {return core.status();}

    public boolean tryApproveByWarehouse() {return core.getState().tryApproveByWarehouse(core);}

    public boolean tryMoveToAwaitingPayment() {return core.getState().tryMoveToAwaitingPayment(core);}

    public boolean tryMarkPaid() {return core.getState().tryMarkPaid(core);}

    public boolean tryMoveToAwaitingDelivery() {return core.getState().tryMoveToAwaitingDelivery(core);}

    public boolean tryMarkReadyForDelivery() {return core.getState().tryMarkReadyForDelivery(core);}

    public boolean tryComplete() {return core.getState().tryComplete(core);}

    public boolean tryCancel() {return core.getState().tryCancel(core);}
}
