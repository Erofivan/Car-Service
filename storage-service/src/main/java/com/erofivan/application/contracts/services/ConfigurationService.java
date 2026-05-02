package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.responses.ConfigurationResponse;

import java.util.List;
import java.util.UUID;

public interface ConfigurationService {
    ConfigurationResponse getDefaultConfiguration(String modelCode);

    ConfigurationResponse buildConfiguration(String modelCode, List<UUID> optionIds);
}
