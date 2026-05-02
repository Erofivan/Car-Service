package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;

import java.util.List;
import java.util.UUID;

public interface CustomOrderService {
    List<CustomOrderResponse> getCustomOrders();

    CustomOrderResponse getCustomOrder(UUID orderId);

    CustomOrderResponse placeCustomOrder(PlaceCustomOrderRequest request);

    CustomOrderResponse cancelCustomOrder(UUID orderId);

    CustomOrderResponse approveCustomOrder(UUID orderId);

    CustomOrderResponse confirmWarehouseCustomOrder(UUID orderId);
}
