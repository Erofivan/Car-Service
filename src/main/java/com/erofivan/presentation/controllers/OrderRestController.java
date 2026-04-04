package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.OrderCatalogService;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderCatalogService orderCatalogService;

    @GetMapping("/inventory")
    public List<InventoryOrderResponse> getInventoryOrders() {
        return orderCatalogService.getInventoryOrders();
    }

    @GetMapping("/custom")
    public List<CustomOrderResponse> getCustomOrders() {
        return orderCatalogService.getCustomOrders();
    }

    @PostMapping("/inventory")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryOrderResponse placeInventoryOrder(@RequestBody PlaceInventoryOrderRequest request) {
        return orderCatalogService.placeInventoryOrder(request);
    }

    @PostMapping("/custom")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomOrderResponse placeCustomOrder(@RequestBody PlaceCustomOrderRequest request) {
        return orderCatalogService.placeCustomOrder(request);
    }
}
