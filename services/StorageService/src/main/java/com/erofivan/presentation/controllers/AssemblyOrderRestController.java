package com.erofivan.presentation.controllers;

import com.erofivan.application.core.services.AssemblyOrderCatalogService;
import com.erofivan.presentation.dtos.requests.CreateAssemblyOrderRequest;
import com.erofivan.presentation.dtos.responses.AssemblyOrderResponse;
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
@RequestMapping("/api/assembly-orders")
@RequiredArgsConstructor
public class AssemblyOrderRestController {
    private final AssemblyOrderCatalogService assemblyOrderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'MANAGER', 'ADMIN')")
    public List<AssemblyOrderResponse> getAssemblyOrders() {
        return assemblyOrderService.getAssemblyOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'MANAGER', 'ADMIN')")
    public AssemblyOrderResponse getAssemblyOrder(@PathVariable UUID id) {
        return assemblyOrderService.getAssemblyOrder(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse createAssemblyOrder(@RequestBody CreateAssemblyOrderRequest request) {
        return assemblyOrderService.createAssemblyOrder(request.sourceOrderId(), request.modelCode());
    }

    @PostMapping("/{id}/assembled")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse markAssembled(@PathVariable UUID id) {
        return assemblyOrderService.markAssembled(id);
    }

    @PostMapping("/{id}/failed")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse markFailed(@PathVariable UUID id) {
        return assemblyOrderService.markFailed(id);
    }
}
