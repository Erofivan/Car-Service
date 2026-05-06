package com.erofivan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import com.erofivan.presentation.dtos.responses.*;
import com.erofivan.presentation.dtos.requests.*;

@SpringBootApplication
@RegisterReflectionForBinding({
    CarAvailabilityResponse.class,
    InventoryOrderResponse.class,
    TestDriveResponse.class,
    CustomOrderResponse.class,
    PlaceCustomOrderRequest.class,
    ScheduleTestDriveRequest.class,
    PlaceInventoryOrderRequest.class
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
