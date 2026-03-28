package com.erofivan.application.contracts.configurations.operations;

import com.erofivan.application.contracts.configurations.dtos.ConfigurationDto;

public final class BuildConfiguration {
    private BuildConfiguration() {
    }

    public record Request(String modelCode) {
    }

    public sealed interface Response permits Success, Failed {
    }

    public record Success(ConfigurationDto configuration) implements Response {
    }

    public record Failed(String message) implements Response {
    }
}
