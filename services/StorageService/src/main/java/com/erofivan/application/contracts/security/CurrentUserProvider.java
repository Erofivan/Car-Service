package com.erofivan.application.contracts.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();

    boolean hasRole(String role);
}
