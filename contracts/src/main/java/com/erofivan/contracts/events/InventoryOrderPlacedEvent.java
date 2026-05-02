package com.erofivan.contracts.events;

import java.util.UUID;

public record InventoryOrderPlacedEvent(UUID orderId, UUID carId) {
}
