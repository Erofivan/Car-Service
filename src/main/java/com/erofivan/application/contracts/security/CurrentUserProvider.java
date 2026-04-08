package com.erofivan.application.contracts.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();

    Boolean hasRole(String role);
}
