package com.erofivan.application.core;

import com.erofivan.domain.cars.CarModel;
import com.erofivan.domain.cars.brands.audi.models.audia4.AudiA4Model;
import com.erofivan.domain.cars.brands.bmw.models.bmw320i.Bmw320iModel;
import com.erofivan.domain.cars.brands.bmw.models.bmw330i.Bmw330iModel;
import com.erofivan.domain.cars.brands.bmw.models.bmwm340i.BmwM340iModel;
import com.erofivan.domain.cars.brands.mercedes.models.mercedese300.MercedesE300Model;
import com.erofivan.domain.exceptions.EntityNotFoundException;

import java.util.List;

public final class ModelDirectory {
    private final List<CarModel> models = List.of(
            new Bmw320iModel(),
            new Bmw330iModel(),
            new BmwM340iModel(),
            new AudiA4Model(),
            new MercedesE300Model()
    );

    public CarModel resolve(String modelCode) {
        return models.stream()
                .filter(model -> model.code().equals(modelCode))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CarModel", modelCode));
    }

    public List<CarModel> all() {
        return models;
    }
}
