package com.erofivan.application.contracts.configurations;

import com.erofivan.application.contracts.configurations.operations.BuildConfiguration;

public interface ConfigurationServiceContract {
    BuildConfiguration.Response buildConfiguration(BuildConfiguration.Request request);
}
