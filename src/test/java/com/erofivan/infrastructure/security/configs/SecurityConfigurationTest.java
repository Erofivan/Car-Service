package com.erofivan.infrastructure.security.configs;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigurationTest {

    private final SecurityConfiguration configuration = new SecurityConfiguration();

    @Test
    void jwtAuthenticationConverterMapsRealmRolesToRoleAuthorities() {
        JwtAuthenticationConverter converter = configuration.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .subject("user-1")
            .claim("realm_access", Map.of("roles", List.of("ADMIN", "MANAGER")))
            .build();

        Set<String> authorities = converter.convert(jwt)
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        assertEquals(Set.of("ROLE_ADMIN", "ROLE_MANAGER"), authorities);
    }

    @Test
    void jwtAuthenticationConverterReturnsEmptyAuthoritiesWhenRealmAccessAbsent() {
        JwtAuthenticationConverter converter = configuration.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .subject("user-1")
            .build();

        assertTrue(converter.convert(jwt).getAuthorities().isEmpty());
    }
}
