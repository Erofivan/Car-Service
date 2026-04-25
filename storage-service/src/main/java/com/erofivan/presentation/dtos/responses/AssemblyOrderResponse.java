package com.erofivan.presentation.dtos.responses;

import com.erofivan.domain.AssemblyStatus;

import java.util.UUID;

public record AssemblyOrderResponse(
    UUID id,
    UUID sourceOrderId,
    String modelCode,
    AssemblyStatus status
) {
}
