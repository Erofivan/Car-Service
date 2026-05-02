package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.CustomOrderCatalogService;
import com.erofivan.application.core.services.InventoryOrderCatalogService;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final InventoryOrderCatalogService inventoryOrderService;
    private final CustomOrderCatalogService customOrderService;

    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<InventoryOrderResponse> getInventoryOrders() {
        return inventoryOrderService.getInventoryOrders();
    }

    @GetMapping("/inventory/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') "
        + "or (hasRole('USER') and @orderSecurity.isInventoryOrderOwner(#id))")
    public InventoryOrderResponse getInventoryOrder(@PathVariable UUID id) {
        return inventoryOrderService.getInventoryOrder(id);
    }

    @PostMapping("/inventory")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public InventoryOrderResponse placeInventoryOrder(@RequestBody PlaceInventoryOrderRequest request) {
        return inventoryOrderService.placeInventoryOrder(request);
    }

    @PostMapping("/inventory/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') "
        + "or (hasRole('USER') and @orderSecurity.isInventoryOrderOwner(#id))")
    public InventoryOrderResponse cancelInventoryOrder(@PathVariable UUID id) {
        return inventoryOrderService.cancelInventoryOrder(id);
    }

    @PostMapping("/inventory/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public InventoryOrderResponse approveInventoryOrder(@PathVariable UUID id) {
        return inventoryOrderService.approveInventoryOrder(id);
    }

    @PostMapping("/inventory/{id}/confirm-warehouse")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public InventoryOrderResponse confirmWarehouseInventoryOrder(@PathVariable UUID id) {
        return inventoryOrderService.confirmWarehouseInventoryOrder(id);
    }

    @GetMapping("/custom")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<CustomOrderResponse> getCustomOrders() {
        return customOrderService.getCustomOrders();
    }

    @GetMapping("/custom/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') "
        + "or (hasRole('USER') and @orderSecurity.isCustomOrderOwner(#id))")
    public CustomOrderResponse getCustomOrder(@PathVariable UUID id) {
        return customOrderService.getCustomOrder(id);
    }

    @PostMapping("/custom")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CustomOrderResponse placeCustomOrder(@RequestBody PlaceCustomOrderRequest request) {
        return customOrderService.placeCustomOrder(request);
    }

    @PostMapping("/custom/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') "
        + "or (hasRole('USER') and @orderSecurity.isCustomOrderOwner(#id))")
    public CustomOrderResponse cancelCustomOrder(@PathVariable UUID id) {
        return customOrderService.cancelCustomOrder(id);
    }

    @PostMapping("/custom/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public CustomOrderResponse approveCustomOrder(@PathVariable UUID id) {
        return customOrderService.approveCustomOrder(id);
    }

    @PostMapping("/custom/{id}/confirm-warehouse")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CustomOrderResponse confirmWarehouseCustomOrder(@PathVariable UUID id) {
        return customOrderService.confirmWarehouseCustomOrder(id);
    }
}
