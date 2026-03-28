package com.erofivan.domain.cars.brands.bmw.models.bmw330i;

import com.erofivan.domain.cars.CarBrand;
import com.erofivan.domain.cars.brands.bmw.BmwBrand;
import com.erofivan.domain.cars.brands.bmw.models.BmwModel;
import com.erofivan.domain.configurations.CarModelSpec;

public final class Bmw330iModel implements BmwModel {
    @Override
    public CarBrand brand() {
        return new BmwBrand();
    }

    @Override
    public String name() {
        return "330i";
    }

    @Override
    public String code() {
        return "BMW-330I";
    }

    @Override
    public CarModelSpec spec() {
        return new Bmw330iModelSpec();
    }
}
