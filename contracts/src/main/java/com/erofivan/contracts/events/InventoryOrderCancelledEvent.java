package com.erofivan.contracts.events;

import java.util.UUID;

public record InventoryOrderCancelledEvent(UUID orderId, UUID carId) {
}
