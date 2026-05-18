package com.erofivan.infrastructure.http;

import com.erofivan.contracts.grpc.cars.AvailableCar;
import com.erofivan.contracts.grpc.cars.GetAvailableCarByIdRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsRequest;
import com.erofivan.contracts.grpc.cars.ListAvailableCarsResponse;
import com.erofivan.contracts.grpc.cars.StorageCarsServiceGrpc;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.exceptions.StorageServiceUnavailableException;
import com.erofivan.presentation.dtos.responses.AvailableCarResponse;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StorageServiceClientGrpcTest {

    private io.grpc.ManagedChannel channel;
    private Server server;

    @AfterEach
    void tearDown() throws InterruptedException {
        if (channel != null) {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        if (server != null) {
            server.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    void getAvailableCarsReturnsMappedCars() throws IOException {
        UUID carId = UUID.randomUUID();

        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void listAvailableCars(
                ListAvailableCarsRequest request,
                StreamObserver<ListAvailableCarsResponse> responseObserver
            ) {
                responseObserver.onNext(ListAvailableCarsResponse.newBuilder()
                    .addCars(grpcCar(carId, true))
                    .build());
                responseObserver.onCompleted();
            }
        }, 1000L);

        List<AvailableCarResponse> cars = client.getAvailableCars();

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).id()).isEqualTo(carId);
        assertThat(cars.get(0).brandCode()).isEqualTo("BMW");
        assertThat(cars.get(0).available()).isTrue();
    }

    @Test
    void getAvailableCarsReturnsEmptyList() throws IOException {
        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void listAvailableCars(
                ListAvailableCarsRequest request,
                StreamObserver<ListAvailableCarsResponse> responseObserver
            ) {
                responseObserver.onNext(ListAvailableCarsResponse.newBuilder().build());
                responseObserver.onCompleted();
            }
        }, 1000L);

        List<AvailableCarResponse> cars = client.getAvailableCars();

        assertThat(cars).isEmpty();
    }

    @Test
    void getAvailableCarByIdThrowsEntityNotFound() throws IOException {
        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void getAvailableCarById(
                GetAvailableCarByIdRequest request,
                StreamObserver<AvailableCar> responseObserver
            ) {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
        }, 1000L);

        assertThatThrownBy(() -> client.getAvailableCarById(UUID.randomUUID()))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getAvailableCarsThrowsStorageUnavailableWhenUnavailable() throws IOException {
        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void listAvailableCars(
                ListAvailableCarsRequest request,
                StreamObserver<ListAvailableCarsResponse> responseObserver
            ) {
                responseObserver.onError(Status.UNAVAILABLE.asRuntimeException());
            }
        }, 1000L);

        assertThatThrownBy(client::getAvailableCars)
            .isInstanceOf(StorageServiceUnavailableException.class);
    }

    @Test
    void getAvailableCarsThrowsStorageUnavailableOnTimeout() throws IOException {
        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void listAvailableCars(
                ListAvailableCarsRequest request,
                StreamObserver<ListAvailableCarsResponse> responseObserver
            ) {
                responseObserver.onError(Status.DEADLINE_EXCEEDED.asRuntimeException());
            }
        }, 5L);

        assertThatThrownBy(client::getAvailableCars)
            .isInstanceOf(StorageServiceUnavailableException.class);
    }

    @Test
    void getCarAvailabilityMapsGrpcResponse() throws IOException {
        UUID carId = UUID.randomUUID();

        StorageServiceClient client = newClient(new StorageCarsServiceGrpc.StorageCarsServiceImplBase() {
            @Override
            public void getAvailableCarById(
                GetAvailableCarByIdRequest request,
                StreamObserver<AvailableCar> responseObserver
            ) {
                responseObserver.onNext(grpcCar(carId, true));
                responseObserver.onCompleted();
            }
        }, 1000L);

        CarAvailabilityResponse response = client.getCarAvailability(carId);

        assertThat(response.id()).isEqualTo(carId);
        assertThat(response.available()).isTrue();
        assertThat(response.testDriveEnabled()).isTrue();
    }

    private StorageServiceClient newClient(
        StorageCarsServiceGrpc.StorageCarsServiceImplBase service,
        long timeoutMs
    ) throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        server = InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(service)
            .build()
            .start();

        channel = InProcessChannelBuilder.forName(serverName)
            .directExecutor()
            .build();

        StorageServiceClient client = new StorageServiceClient(
            StorageCarsServiceGrpc.newBlockingStub(channel)
        );
        ReflectionTestUtils.setField(client, "timeoutMs", timeoutMs);
        return client;
    }

    private AvailableCar grpcCar(UUID carId, boolean testDriveEnabled) {
        return AvailableCar.newBuilder()
            .setId(carId.toString())
            .setBrand("BMW")
            .setBrandCode("BMW")
            .setModel("320i")
            .setModelCode("BMW-320I")
            .setBodyType("Sedan")
            .setFuelType("Petrol")
            .setPowerHp(184)
            .setEngineLitres(2.0)
            .setTransmission("Automatic 8AT")
            .setDrivetrain("Rear")
            .setColor("Black")
            .setPrice(4200000L)
            .setAvailable(true)
            .setTestDriveEnabled(testDriveEnabled)
            .build();
    }
}
