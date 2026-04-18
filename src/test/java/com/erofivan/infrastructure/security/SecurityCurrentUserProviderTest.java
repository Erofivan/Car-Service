package com.erofivan.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityCurrentUserProviderTest {

    private final SecurityCurrentUserProvider provider = new SecurityCurrentUserProvider();

    @AfterEach
    void cleanupSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserIdReturnsJwtSubject() {
        String userId = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .subject(userId)
            .build();

        SecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken(jwt, null, "ROLE_USER")
        );

        assertEquals(UUID.fromString(userId), provider.getCurrentUserId());
    }

    @Test
    void hasRoleReturnsTrueWhenRoleExists() {
        SecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken(
                "principal",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))
            )
        );

        assertTrue(provider.hasRole("MANAGER"));
        assertFalse(provider.hasRole("ADMIN"));
    }

    @Test
    void getCurrentUserIdThrowsWhenNoJwtInContext() {
        SecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken("principal", null, "ROLE_USER")
        );

        assertThrows(IllegalStateException.class, provider::getCurrentUserId);
    }
}
