package com.erofivan.domain.cars.brands.bmw;

import com.erofivan.domain.cars.CarBrand;

public final class BmwBrand implements CarBrand {
    @Override
    public String displayName() {
        return "BMW";
    }

    @Override
    public String code() {
        return "BMW";
    }
}
