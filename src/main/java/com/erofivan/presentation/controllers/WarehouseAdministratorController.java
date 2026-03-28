package com.erofivan.presentation.controllers;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.abstractions.persistence.queries.OrderQuery;
import com.erofivan.application.contracts.parts.PartServiceContract;
import lombok.AllArgsConstructor;

import java.util.logging.Logger;

@AllArgsConstructor
public final class WarehouseAdministratorController {
    private static final Logger LOGGER = Logger.getLogger(WarehouseAdministratorController.class.getName());

    private final IPersistenceContext context;
    private final PartServiceContract partService;

    public void run() {
        LOGGER.info("Warehouse: list parts");
        partService.listParts().forEach(part -> LOGGER.info("- " + part.name() + " " + part.price()));

        LOGGER.info("Warehouse: process custom orders");
        context.customOrders().query(OrderQuery.builder().status("PLACED").build())
            .forEach(order -> {
                order.tryApproveByWarehouse();
                order.tryMoveToAwaitingPayment();
                context.customOrders().update(order);
                LOGGER.info("- custom order moved to: " + order.status());
            });
    }
}
