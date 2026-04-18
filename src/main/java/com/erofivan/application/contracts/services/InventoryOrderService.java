package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryOrderService {
    List<InventoryOrderResponse> getInventoryOrders();

    InventoryOrderResponse getInventoryOrder(UUID orderId);

    InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request);

    InventoryOrderResponse cancelInventoryOrder(UUID orderId);

    InventoryOrderResponse approveInventoryOrder(UUID orderId);

    InventoryOrderResponse confirmWarehouseInventoryOrder(UUID orderId);
}
