package com.erofivan.infrastructure.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient storageServiceRestClient(
        RestClient.Builder restClientBuilder,
        @Value("${storage.service.base-url}") String baseUrl
    ) {
        return restClientBuilder
            .baseUrl(baseUrl)
            .requestInterceptor((request, body, execution) -> {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth instanceof JwtAuthenticationToken jwtAuth) {
                    request.getHeaders().setBearerAuth(jwtAuth.getToken().getTokenValue());
                }
                return execution.execute(request, body);
            })
            .build();
    }
}
