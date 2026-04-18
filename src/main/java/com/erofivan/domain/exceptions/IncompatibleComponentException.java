package com.erofivan.domain.exceptions;

public final class IncompatibleComponentException extends RuntimeException {
    public IncompatibleComponentException(String optionName, String modelCode) {
        super("Component option '" + optionName + "' is not compatible with model '" + modelCode + "'");
    }
}
