package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.responses.AssemblyOrderResponse;

import java.util.List;
import java.util.UUID;

public interface AssemblyOrderService {
    List<AssemblyOrderResponse> getAssemblyOrders();

    AssemblyOrderResponse getAssemblyOrder(UUID id);

    AssemblyOrderResponse createAssemblyOrder(UUID sourceOrderId, String modelCode);

    AssemblyOrderResponse markAssembled(UUID id);

    AssemblyOrderResponse markFailed(UUID id);
}
