package com.erofivan.infrastructure.grpc;

import com.erofivan.contracts.grpc.cars.StorageCarsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageGrpcClientConfiguration {

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel storageServiceGrpcChannel(
        @Value("${storage.grpc.host:localhost}") String host,
        @Value("${storage.grpc.port:9090}") int port
    ) {
        return ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build();
    }

    @Bean
    public StorageCarsServiceGrpc.StorageCarsServiceBlockingStub storageCarsServiceBlockingStub(
        ManagedChannel storageServiceGrpcChannel
    ) {
        return StorageCarsServiceGrpc.newBlockingStub(storageServiceGrpcChannel);
    }
}
