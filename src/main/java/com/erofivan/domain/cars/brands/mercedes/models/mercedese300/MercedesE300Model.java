package com.erofivan.domain.cars.brands.mercedes.models.mercedese300;

import com.erofivan.domain.cars.CarBrand;
import com.erofivan.domain.cars.brands.mercedes.MercedesBrand;
import com.erofivan.domain.cars.brands.mercedes.models.MercedesModel;
import com.erofivan.domain.configurations.CarModelSpec;

public final class MercedesE300Model implements MercedesModel {
    @Override
    public CarBrand brand() {
        return new MercedesBrand();
    }

    @Override
    public String name() {
        return "E300";
    }

    @Override
    public String code() {
        return "MERCEDES-E300";
    }

    @Override
    public CarModelSpec spec() {
        return new MercedesE300ModelSpec();
    }
}
