package com.erofivan.presentation.dtos.requests;

import java.util.UUID;

public record CreateAssemblyOrderRequest(
    UUID sourceOrderId,
    String modelCode
) {
}
