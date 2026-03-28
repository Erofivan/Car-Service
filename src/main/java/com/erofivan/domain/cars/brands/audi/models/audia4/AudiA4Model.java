package com.erofivan.domain.cars.brands.audi.models.audia4;

import com.erofivan.domain.cars.CarBrand;
import com.erofivan.domain.cars.brands.audi.AudiBrand;
import com.erofivan.domain.cars.brands.audi.models.AudiModel;
import com.erofivan.domain.configurations.CarModelSpec;

public final class AudiA4Model implements AudiModel {
    @Override
    public CarBrand brand() {
        return new AudiBrand();
    }

    @Override
    public String name() {
        return "A4";
    }

    @Override
    public String code() {
        return "AUDI-A4";
    }

    @Override
    public CarModelSpec spec() {
        return new AudiA4ModelSpec();
    }
}
