package com.erofivan.domain;

public enum OrderStatus {
    PLACED,
    APPROVED_BY_MANAGER,
    APPROVED_BY_WAREHOUSE,
    AWAITING_PAYMENT,
    PAID,
    AWAITING_DELIVERY,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED
}
