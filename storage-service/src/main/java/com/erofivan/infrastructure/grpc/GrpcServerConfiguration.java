package com.erofivan.infrastructure.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server storageGrpcServer(
        StorageCarsGrpcService storageCarsGrpcService,
        @Value("${grpc.server.port:9090}") int grpcPort
    ) throws IOException {
        return NettyServerBuilder.forPort(grpcPort)
            .addService(storageCarsGrpcService)
            .build();
    }
}
