package com.erofivan.infrastructure.http;

import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageServiceClient {
    private final RestClient storageServiceRestClient;

    public CarAvailabilityResponse getCarAvailability(UUID carId) {
        return storageServiceRestClient.get()
            .uri("/api/cars/{carId}/availability", carId)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                (req, res) -> {
                    throw new EntityNotFoundException("Car", carId.toString());
                }
            )
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                (req, res) -> {
                    throw new DomainValidationException(
                        "Storage service error: " + res.getStatusCode());
                }
            )
            .body(CarAvailabilityResponse.class);
    }
}
