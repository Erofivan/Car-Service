package com.erofivan.presentation.dtos.requests;

import java.util.List;
import java.util.UUID;

public record BuildConfigurationRequest(
    List<UUID> optionIds
) {
}
