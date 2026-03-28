package com.erofivan.application.contracts.orders.operations;

import com.erofivan.application.contracts.orders.dtos.InventoryOrderDto;

public final class PlaceInventoryOrder {
    private PlaceInventoryOrder() {
    }

    public sealed interface Response permits Success, Failed {
    }

    public record Request(String clientId, String carId) {
    }

    public record Success(InventoryOrderDto order) implements Response {
    }

    public record Failed(String message) implements Response {
    }
}
