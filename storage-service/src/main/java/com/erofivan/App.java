package com.erofivan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import com.erofivan.presentation.dtos.*;
import com.erofivan.presentation.dtos.responses.*;
import com.erofivan.presentation.dtos.requests.*;
import com.erofivan.contracts.events.*;

@SpringBootApplication
@RegisterReflectionForBinding({
    ConfigurationOption.class,
    CarAvailabilityResponse.class,
    AssemblyOrderResponse.class,
    ConfigurationResponse.class,
    PartResponse.class,
    CarResponse.class,
    CreateAssemblyOrderRequest.class,
    BuildConfigurationRequest.class,
    CustomOrderWarehouseApprovedEvent.class,
    InventoryOrderCancelledEvent.class,
    InventoryOrderPlacedEvent.class
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
