package com.erofivan.application.contracts.orders.dtos;

public record InventoryOrderDto(String id, String clientId, String managerId, String carId,
                                String status) {
}
