package com.erofivan.domain.cars.brands.bmw.models.bmw320i;

import com.erofivan.domain.cars.CarBrand;
import com.erofivan.domain.cars.brands.bmw.BmwBrand;
import com.erofivan.domain.cars.brands.bmw.models.BmwModel;
import com.erofivan.domain.configurations.CarModelSpec;

public final class Bmw320iModel implements BmwModel {
    @Override
    public CarBrand brand() {
        return new BmwBrand();
    }

    @Override
    public String name() {
        return "320i";
    }

    @Override
    public String code() {
        return "BMW-320I";
    }

    @Override
    public CarModelSpec spec() {
        return new Bmw320iModelSpec();
    }
}
