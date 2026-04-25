package com.erofivan.contracts.events;

import java.util.UUID;

public record CustomOrderWarehouseApprovedEvent(UUID orderId, String modelCode) {
}
