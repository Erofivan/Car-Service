package com.erofivan.presentation.controllers;

import com.erofivan.application.core.OrderCatalogService;
import com.erofivan.presentation.dto.CustomOrderResponse;
import com.erofivan.presentation.dto.InventoryOrderResponse;
import com.erofivan.presentation.dto.PlaceCustomOrderRequest;
import com.erofivan.presentation.dto.PlaceInventoryOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderCatalogService orderCatalogService;

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
