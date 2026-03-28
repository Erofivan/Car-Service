package com.erofivan.domain.orders.custom.states;

import com.erofivan.domain.orders.custom.CustomOrderCore;

public interface CustomOrderState {
    default boolean tryApproveByWarehouse(CustomOrderCore core) { return false; }

    default boolean tryMoveToAwaitingPayment(CustomOrderCore core) { return false; }

    default boolean tryMarkPaid(CustomOrderCore core) { return false; }

    default boolean tryMoveToAwaitingDelivery(CustomOrderCore core) { return false; }

    default boolean tryMarkReadyForDelivery(CustomOrderCore core) { return false; }

    default boolean tryComplete(CustomOrderCore core) { return false; }

    default boolean tryCancel(CustomOrderCore core) { return false; }

    String statusName();
}
