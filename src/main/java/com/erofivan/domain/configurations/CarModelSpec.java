package com.erofivan.domain.configurations;

import com.erofivan.domain.common.Money;

import java.util.List;

public interface CarModelSpec {
    String modelCode();

    Money basePrice();

    List<ComponentSlot> slots();
}
