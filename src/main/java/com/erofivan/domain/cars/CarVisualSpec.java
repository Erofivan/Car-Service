package com.erofivan.domain.cars;

import com.erofivan.domain.cars.bodytypes.BodyType;
import com.erofivan.domain.cars.colors.CarColor;

public record CarVisualSpec(BodyType bodyType, CarColor color) {
}
