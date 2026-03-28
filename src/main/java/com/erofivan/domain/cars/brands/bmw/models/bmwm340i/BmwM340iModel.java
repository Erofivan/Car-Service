package com.erofivan.domain.cars.brands.bmw.models.bmwm340i;

import com.erofivan.domain.cars.CarBrand;
import com.erofivan.domain.cars.brands.bmw.BmwBrand;
import com.erofivan.domain.cars.brands.bmw.models.BmwModel;
import com.erofivan.domain.configurations.CarModelSpec;

public final class BmwM340iModel implements BmwModel {
    @Override
    public CarBrand brand() {
        return new BmwBrand();
    }

    @Override
    public String name() {
        return "M340i";
    }

    @Override
    public String code() {
        return "BMW-M340I";
    }

    @Override
    public CarModelSpec spec() {
        return new BmwM340iModelSpec();
    }
}
