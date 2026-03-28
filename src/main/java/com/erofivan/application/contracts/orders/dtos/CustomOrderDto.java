package com.erofivan.application.contracts.orders.dtos;

public record CustomOrderDto(String id, String clientId, String managerId, String modelCode, String status, long totalPrice) {
}
