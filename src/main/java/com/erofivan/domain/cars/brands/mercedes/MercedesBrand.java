package com.erofivan.domain.cars.brands.mercedes;

import com.erofivan.domain.cars.CarBrand;

public final class MercedesBrand implements CarBrand {
    @Override
    public String displayName() {
        return "Mercedes-Benz";
    }

    @Override
    public String code() {
        return "MERCEDES";
    }
}
