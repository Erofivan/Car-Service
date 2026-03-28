package com.erofivan.domain.cars.brands.audi;

import com.erofivan.domain.cars.CarBrand;

public final class AudiBrand implements CarBrand {
    @Override
    public String displayName() {
        return "Audi";
    }

    @Override
    public String code() {
        return "AUDI";
    }
}
