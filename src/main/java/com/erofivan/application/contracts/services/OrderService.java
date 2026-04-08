package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<InventoryOrderResponse> getInventoryOrders();

    List<CustomOrderResponse> getCustomOrders();

    InventoryOrderResponse getInventoryOrder(UUID orderId);

    CustomOrderResponse getCustomOrder(UUID orderId);

    InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request);

    CustomOrderResponse placeCustomOrder(PlaceCustomOrderRequest request);

    InventoryOrderResponse cancelInventoryOrder(UUID orderId);

    CustomOrderResponse cancelCustomOrder(UUID orderId);

    InventoryOrderResponse approveInventoryOrder(UUID orderId);

    CustomOrderResponse approveCustomOrder(UUID orderId);

    InventoryOrderResponse confirmWarehouseInventoryOrder(UUID orderId);

    CustomOrderResponse confirmWarehouseCustomOrder(UUID orderId);
}
