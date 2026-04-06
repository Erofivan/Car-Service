package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;

import java.util.List;

public interface OrderService {
    List<InventoryOrderResponse> getInventoryOrders();

    public List<CustomOrderResponse> getCustomOrders();

    InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request);

    CustomOrderResponse placeCustomOrder(PlaceCustomOrderRequest request);
}
