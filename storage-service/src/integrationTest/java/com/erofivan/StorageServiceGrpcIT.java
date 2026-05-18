package com.erofivan;

import com.erofivan.contracts.grpc.cars.AvailableCar;
import com.erofivan.contracts.grpc.cars.GetAvailableCarByIdRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsResponse;
import com.erofivan.contracts.grpc.cars.StorageCarsServiceGrpc;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StorageServiceGrpcIT extends AbstractStorageServiceIT {

    @Autowired
    private Server storageGrpcServer;

    @Autowired
    private CarRepository carRepository;

    private ManagedChannel channel;
    private StorageCarsServiceGrpc.StorageCarsServiceBlockingStub stub;

    @BeforeEach
    void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", storageGrpcServer.getPort())
            .usePlaintext()
            .build();
        stub = StorageCarsServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    void tearDown() {
        if (channel != null) {
            channel.shutdownNow();
        }
    }

    @Test
    void listAvailableCarsReturnsOnlyAvailableCars() {
        ListAvailableCarsResponse response = stub.listAvailableCars(
            ListAvailableCarsRequest.newBuilder().build());

        assertThat(response.getCarsList()).isNotEmpty();
        assertThat(response.getCarsList()).allMatch(AvailableCar::getAvailable);
    }

    @Test
    void getAvailableCarByIdReturnsCar() {
        ListAvailableCarsResponse listResponse = stub.listAvailableCars(
            ListAvailableCarsRequest.newBuilder().build());

        String carId = listResponse.getCars(0).getId();

        AvailableCar response = stub.getAvailableCarById(
            GetAvailableCarByIdRequest.newBuilder().setId(carId).build());

        assertThat(response.getId()).isEqualTo(carId);
        assertThat(response.getAvailable()).isTrue();
    }

    @Test
    void getAvailableCarByIdReturnsNotFoundForUnknownCar() {
        StatusRuntimeException exception = Assertions.assertThrows(
            StatusRuntimeException.class,
            () -> stub.getAvailableCarById(
                GetAvailableCarByIdRequest.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .build()
            )
        );

        assertThat(exception.getStatus().getCode()).isEqualTo(Status.Code.NOT_FOUND);
    }

    @Test
    void listAvailableCarsReturnsEmptyWhenAllCarsUnavailable() {
        List<CarEntity> cars = carRepository.findAll();
        Map<UUID, Boolean> originalAvailability = cars.stream()
            .collect(Collectors.toMap(CarEntity::getId, CarEntity::isAvailable));

        try {
            cars.forEach(car -> car.setAvailable(false));
            carRepository.saveAll(cars);
            carRepository.flush();

            ListAvailableCarsResponse response = stub.listAvailableCars(
                ListAvailableCarsRequest.newBuilder().build());

            assertThat(response.getCarsCount()).isZero();
        } finally {
            List<CarEntity> actualCars = carRepository.findAll();
            actualCars.forEach(car -> car.setAvailable(Boolean.TRUE.equals(
                originalAvailability.get(car.getId()))));
            carRepository.saveAll(actualCars);
            carRepository.flush();
        }
    }
}
