package com.erofivan.presentation.controllers;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import lombok.AllArgsConstructor;

import java.util.logging.Logger;

@AllArgsConstructor
public final class ManagerController {
    private static final Logger LOGGER = Logger.getLogger(ManagerController.class.getName());

    private final IPersistenceContext context;

    public void run() {
        LOGGER.info("Manager: process inventory orders");
        context.inventoryOrders().query(OrderQuery.builder().status("PLACED").build())
            .forEach(order -> {
                order.tryApproveByManager();
                order.tryMoveToAwaitingPayment();
                context.inventoryOrders().update(order);
                LOGGER.info("- inventory order moved to: " + order.status());
            });
    }
}
