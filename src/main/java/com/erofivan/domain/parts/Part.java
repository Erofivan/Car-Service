package com.erofivan.domain.parts;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.common.ids.PartId;
import lombok.Getter;

@Getter
public final class Part {
    private final PartId id;
    private final String name;
    private final PartCompatibility compatibility;
    private String description;
    private Money price;

    public Part(PartId id, String name, String description, Money price, PartCompatibility compatibility) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.compatibility = compatibility;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
    }
}
