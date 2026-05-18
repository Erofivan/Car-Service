package com.erofivan.infrastructure.http;

import com.erofivan.contracts.grpc.cars.AvailableCar;
import com.erofivan.contracts.grpc.cars.GetAvailableCarByIdRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsResponse;
import com.erofivan.contracts.grpc.cars.StorageCarsServiceGrpc;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.exceptions.StorageServiceUnavailableException;
import com.erofivan.presentation.dtos.responses.AvailableCarResponse;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class StorageServiceClient {
    private final StorageCarsServiceGrpc.StorageCarsServiceBlockingStub storageCarsStub;

    @Value("${storage.grpc.timeout-ms:2000}")
    private long timeoutMs;

    public CarAvailabilityResponse getCarAvailability(UUID carId) {
        AvailableCar car = getAvailableCar(carId);
        return new CarAvailabilityResponse(carId, car.getAvailable(), car.getTestDriveEnabled());
    }

    public List<AvailableCarResponse> getAvailableCars() {
        try {
            ListAvailableCarsResponse response = storageCarsStub
                .withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                .listAvailableCars(ListAvailableCarsRequest.newBuilder().build());

            log.info("Fetched {} available cars from storage over gRPC", response.getCarsCount());

            return response.getCarsList()
                .stream()
                .map(this::toAvailableCarResponse)
                .toList();
        } catch (StatusRuntimeException exception) {
            throw mapGrpcException(exception, "Car", null);
        }
    }

    public AvailableCarResponse getAvailableCarById(UUID carId) {
        return toAvailableCarResponse(getAvailableCar(carId));
    }

    private AvailableCar getAvailableCar(UUID carId) {
        try {
            return storageCarsStub
                .withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                .getAvailableCarById(
                    GetAvailableCarByIdRequest.newBuilder()
                        .setId(carId.toString())
                        .build()
                );
        } catch (StatusRuntimeException exception) {
            throw mapGrpcException(exception, "Car", carId.toString());
        }
    }

    private AvailableCarResponse toAvailableCarResponse(AvailableCar grpcCar) {
        return new AvailableCarResponse(
            UUID.fromString(grpcCar.getId()),
            grpcCar.getBrand(),
            grpcCar.getBrandCode(),
            grpcCar.getModel(),
            grpcCar.getModelCode(),
            grpcCar.getBodyType(),
            grpcCar.getFuelType(),
            grpcCar.getPowerHp(),
            grpcCar.getEngineLitres(),
            grpcCar.getTransmission(),
            grpcCar.getDrivetrain(),
            grpcCar.getColor(),
            grpcCar.getPrice(),
            grpcCar.getAvailable(),
            grpcCar.getTestDriveEnabled()
        );
    }

    private RuntimeException mapGrpcException(
        StatusRuntimeException exception,
        String entity,
        String identifier
    ) {
        Status.Code code = exception.getStatus().getCode();
        String description = exception.getStatus().getDescription();

        if (code == Status.Code.NOT_FOUND) {
            String entityId = identifier == null ? "unknown" : identifier;
            return new EntityNotFoundException(entity, entityId);
        }

        if (code == Status.Code.UNAVAILABLE || code == Status.Code.DEADLINE_EXCEEDED) {
            return new StorageServiceUnavailableException(
                "Storage service is temporarily unavailable"
            );
        }

        if (code == Status.Code.INVALID_ARGUMENT) {
            return new DomainValidationException(
                description == null ? "Invalid request to storage service" : description
            );
        }

        HttpStatus status = code == Status.Code.UNKNOWN
            ? HttpStatus.INTERNAL_SERVER_ERROR
            : HttpStatus.BAD_REQUEST;

        return new DomainValidationException(
            "Storage service error: " + status + (description == null ? "" : "; " + description)
        );
    }
}
