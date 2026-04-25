package com.erofivan.domain.exceptions;

public final class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityType, String entityId) {
        super(entityType + " with id '" + entityId + "' not found");
    }
}
