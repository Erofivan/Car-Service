package com.erofivan.domain.exceptions;

public final class MissingRequiredSlotException extends DomainValidationException {
    public MissingRequiredSlotException(String slotName) {
        super("Missing required slot '" + slotName + "'");
    }
}
