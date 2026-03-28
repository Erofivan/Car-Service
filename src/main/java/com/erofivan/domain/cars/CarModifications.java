package com.erofivan.domain.cars;

import com.erofivan.domain.cars.bodytypes.BodyType;
import com.erofivan.domain.cars.colors.CarColor;

public record CarModifications(BodyType bodyType, CarColor color) {
}
