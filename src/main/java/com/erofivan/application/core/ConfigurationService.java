package com.erofivan.application.core;

import com.erofivan.application.contracts.configurations.ConfigurationServiceContract;
import com.erofivan.application.contracts.configurations.operations.BuildConfiguration;
import com.erofivan.application.core.mappings.ConfigurationMappings;
import com.erofivan.domain.cars.CarModel;
import com.erofivan.domain.configurations.CarConfiguration;
import com.erofivan.domain.configurations.CarConfigurator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ConfigurationService implements ConfigurationServiceContract {
    private final ModelDirectory modelDirectory;

    @Override
    public BuildConfiguration.Response buildConfiguration(BuildConfiguration.Request request) {
        try {
            CarModel model = modelDirectory.resolve(request.modelCode());
            CarConfigurator configurator = new CarConfigurator(model.spec());
            model.spec().slots().forEach(slot -> configurator.select(slot.slotName(), slot.baseOption().name()));
            CarConfiguration configuration = configurator.build();
            return new BuildConfiguration.Success(ConfigurationMappings.toDto(model.spec(), configuration));
        } catch (RuntimeException exception) {
            return new BuildConfiguration.Failed(exception.getMessage());
        }
    }
}
