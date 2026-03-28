package com.erofivan.domain.cars;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.common.ids.CarId;
import lombok.Getter;

@Getter
public final class Car {
    private final CarId id;
    private final CarModel model;
    private final CarStaticSpec staticSpec;
    private CarVisualSpec visualSpec;
    private Money price;
    private boolean available;
    private boolean testDriveEnabled;

    public Car(
            CarId id,
            CarModel model,
            CarStaticSpec staticSpec,
            CarVisualSpec visualSpec,
            Money price,
            boolean available,
            boolean testDriveEnabled
    ) {
        this.id = id;
        this.model = model;
        this.staticSpec = staticSpec;
        this.visualSpec = visualSpec;
        this.price = price;
        this.available = available;
        this.testDriveEnabled = testDriveEnabled;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
    }

    public void updateVisualSpec(CarVisualSpec newVisualSpec) {
        this.visualSpec = newVisualSpec;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setTestDriveEnabled(boolean testDriveEnabled) {
        this.testDriveEnabled = testDriveEnabled;
    }

    public String modelCode() {
        return model.code();
    }

    public String brandCode() {
        return model.brand().code();
    }
}
