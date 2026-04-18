package com.erofivan.infrastructure.security;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityCurrentUserProvider implements CurrentUserProvider {

    @Override
    public UUID getCurrentUserId() {
        Jwt jwt = getCurrentJwt();
        return UUID.fromString(jwt.getSubject());
    }

    @Override
    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("No JWT authentication found in SecurityContext");
        }
        return jwt;
    }
}
