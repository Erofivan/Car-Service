package com.erofivan.application.core;

import com.erofivan.application.contracts.configurations.operations.BuildConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ConfigurationServiceTest {

    @Test
    void shouldBuildDefaultConfigurationForKnownModel() {
        // arrange
        var service = new ConfigurationService(new ModelDirectory());

        // act
        var response = service.buildConfiguration(new BuildConfiguration.Request("BMW-320I"));

        // assert
        var success = assertInstanceOf(BuildConfiguration.Success.class, response);
        assertEquals("BMW-320I", success.configuration().modelCode());
        assertEquals(4_000_000L, success.configuration().totalPrice());
    }

    @Test
    void shouldReturnFailedForUnknownModel() {
        // arrange
        var service = new ConfigurationService(new ModelDirectory());

        // act
        var response = service.buildConfiguration(new BuildConfiguration.Request("UNKNOWN-MODEL"));

        // assert
        assertInstanceOf(BuildConfiguration.Failed.class, response);
    }
}
