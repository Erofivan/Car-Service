package com.erofivan.application.core.services;

import com.erofivan.infrastructure.http.StorageServiceClient;
import com.erofivan.presentation.dtos.responses.AvailableCarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvailableCarCatalogService {
    private final StorageServiceClient storageServiceClient;

    public List<AvailableCarResponse> getAvailableCars() {
        return storageServiceClient.getAvailableCars();
    }

    public AvailableCarResponse getAvailableCarById(UUID carId) {
        return storageServiceClient.getAvailableCarById(carId);
    }
}
