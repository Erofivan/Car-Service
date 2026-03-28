package com.erofivan.domain.orders.inventory.states;

import com.erofivan.domain.orders.inventory.InventoryOrderCore;

public interface InventoryOrderState {
    default boolean tryApproveByManager(InventoryOrderCore core) {return false;}

    default boolean tryMoveToAwaitingPayment(InventoryOrderCore core) {return false;}

    default boolean tryMarkPaid(InventoryOrderCore core) {return false;}

    default boolean tryMarkReadyForDelivery(InventoryOrderCore core) {return false;}

    default boolean tryComplete(InventoryOrderCore core) {return false;}

    default boolean tryCancel(InventoryOrderCore core) {return false;}

    String statusName();
}
