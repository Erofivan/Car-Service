package com.erofivan.infrastructure.grpc;

import com.erofivan.application.core.services.CarCatalogService;
import com.erofivan.contracts.grpc.cars.AvailableCar;
import com.erofivan.contracts.grpc.cars.GetAvailableCarByIdRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsResponse;
import com.erofivan.contracts.grpc.cars.StorageCarsServiceGrpc;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.presentation.dtos.responses.CarResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageCarsGrpcService extends StorageCarsServiceGrpc.StorageCarsServiceImplBase {

    private final CarCatalogService carCatalogService;

    @Override
    public void listAvailableCars(
        ListAvailableCarsRequest request,
        StreamObserver<ListAvailableCarsResponse> responseObserver
    ) {
        try {
            List<AvailableCar> cars = carCatalogService.getAvailableCars()
                .stream()
                .map(this::toGrpcCar)
                .toList();

            log.info("gRPC listAvailableCars: returning {} cars", cars.size());

            responseObserver.onNext(ListAvailableCarsResponse.newBuilder()
                .addAllCars(cars)
                .build());
            responseObserver.onCompleted();
        } catch (Exception exception) {
            log.error("gRPC listAvailableCars failed", exception);
            responseObserver.onError(Status.INTERNAL
                .withDescription("Failed to load available cars")
                .withCause(exception)
                .asRuntimeException());
        }
    }

    @Override
    public void getAvailableCarById(
        GetAvailableCarByIdRequest request,
        StreamObserver<AvailableCar> responseObserver
    ) {
        UUID carId;
        try {
            carId = UUID.fromString(request.getId());
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Invalid car id: " + request.getId())
                .withCause(exception)
                .asRuntimeException());
            return;
        }

        try {
            CarResponse car = carCatalogService.getAvailableCarById(carId);
            log.info("gRPC getAvailableCarById: id={}", carId);
            responseObserver.onNext(toGrpcCar(car));
            responseObserver.onCompleted();
        } catch (EntityNotFoundException exception) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription(exception.getMessage())
                .withCause(exception)
                .asRuntimeException());
        } catch (Exception exception) {
            log.error("gRPC getAvailableCarById failed for id={}", carId, exception);
            responseObserver.onError(Status.INTERNAL
                .withDescription("Failed to load available car")
                .withCause(exception)
                .asRuntimeException());
        }
    }

    private AvailableCar toGrpcCar(CarResponse response) {
        return AvailableCar.newBuilder()
            .setId(response.id().toString())
            .setBrand(response.brand())
            .setBrandCode(response.brandCode())
            .setModel(response.model())
            .setModelCode(response.modelCode())
            .setBodyType(response.bodyType())
            .setFuelType(response.fuelType())
            .setPowerHp(response.powerHp())
            .setEngineLitres(response.engineLitres())
            .setTransmission(response.transmission())
            .setDrivetrain(response.drivetrain())
            .setColor(response.color())
            .setPrice(response.price())
            .setAvailable(Boolean.TRUE.equals(response.available()))
            .setTestDriveEnabled(Boolean.TRUE.equals(response.testDriveEnabled()))
            .build();
    }
}
