package com.erofivan.application.contracts.orders.operations;

import com.erofivan.application.contracts.orders.dtos.CustomOrderDto;

import java.util.Map;

public final class PlaceCustomOrder {
    private PlaceCustomOrder() {
    }

    public sealed interface Response permits Success, Failed {
    }

    public record Request(String clientId, String modelCode, Map<String, String> selectedOptions) {
    }

    public record Success(CustomOrderDto order) implements Response {
    }

    public record Failed(String message) implements Response {
    }
}
