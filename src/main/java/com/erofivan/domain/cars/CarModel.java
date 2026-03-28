package com.erofivan.domain.cars;

import com.erofivan.domain.configurations.CarModelSpec;

public interface CarModel {
    CarBrand brand();

    String name();

    String code();

    CarModelSpec spec();
}
