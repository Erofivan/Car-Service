package com.erofivan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import liquibase.ui.CompositeUIService;
import liquibase.ui.ConsoleUIService;
import liquibase.ui.LoggerUIService;
import liquibase.changelog.FastCheckService;

import com.fasterxml.jackson.databind.JsonNode;

import com.erofivan.presentation.dtos.*;
import com.erofivan.presentation.dtos.responses.*;
import com.erofivan.presentation.dtos.requests.*;
import com.erofivan.contracts.events.*;

@SpringBootApplication
@ImportRuntimeHints(App.LiquibaseNativeHints.class)
@RegisterReflectionForBinding({
    ConfigurationOption.class,
    CarAvailabilityResponse.class,
    AssemblyOrderResponse.class,
    ConfigurationResponse.class,
    PartResponse.class,
    CarResponse.class,
    CreateAssemblyOrderRequest.class,
    BuildConfigurationRequest.class,
    JsonNode.class,
    CustomOrderWarehouseApprovedEvent.class,
    InventoryOrderCancelledEvent.class,
    InventoryOrderPlacedEvent.class
})
public class App {

    static class LiquibaseNativeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(
                FastCheckService.class,
                MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
            );
            hints.reflection().registerType(
                LoggerUIService.class,
                MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
            );
            hints.reflection().registerType(
                ConsoleUIService.class,
                MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
            );
            hints.reflection().registerType(
                CompositeUIService.class,
                MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
            );
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
