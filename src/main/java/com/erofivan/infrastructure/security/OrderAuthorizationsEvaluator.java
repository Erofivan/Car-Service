package com.erofivan.infrastructure.security;

import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("orderSecurity")
@RequiredArgsConstructor
public class OrderAuthorizationsEvaluator {

    private final InventoryOrderRepository inventoryOrderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    public boolean isInventoryOrderOwner(UUID orderId) {
        UUID currentUserId = securityUtils.getCurrentUserId();
        return inventoryOrderRepository.findById(orderId)
            .map(order -> order.getClient().getId().equals(currentUserId))
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean isCustomOrderOwner(UUID orderId) {
        UUID currentUserId = securityUtils.getCurrentUserId();
        return customOrderRepository.findById(orderId)
            .map(order -> order.getClient().getId().equals(currentUserId))
            .orElse(false);
    }
}
