package com.erofivan.domain.exceptions;

public final class IncompatibleComponentException extends RuntimeException {
    public IncompatibleComponentException(String componentName, String modelCode) {
        super("Component '" + componentName + "' is not compatible with model '" + modelCode + "'");
    }
}
