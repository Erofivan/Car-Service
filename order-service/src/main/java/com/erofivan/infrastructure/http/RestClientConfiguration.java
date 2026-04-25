package com.erofivan.infrastructure.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient storageServiceRestClient(
        @Value("${storage.service.base-url}") String baseUrl
    ) {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
