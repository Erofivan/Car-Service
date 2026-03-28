package com.erofivan.domain.exceptions;

public final class InvalidOrderStateTransitionException extends RuntimeException {
    public InvalidOrderStateTransitionException(String currentState, String action) {
        super("Cannot perform '" + action + "' in state '" + currentState + "'");
    }
}
