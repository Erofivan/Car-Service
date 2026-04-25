package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.infrastructure.persistence.jpa.mappers.CarMapper;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import com.erofivan.presentation.dtos.responses.CarResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarCatalogServiceTest {

    @Mock private CarRepository carRepository;
    @Mock private CarMapper carMapper;

    @InjectMocks
    private CarCatalogService service;

    private static final UUID CAR_ID = UUID.randomUUID();

    @Test
    void getCarByIdReturnsResponse() {
        CarEntity car = createCar(CAR_ID, false, false);
        CarResponse expected = new CarResponse(CAR_ID, "BMW", "BMW", "3 Series", "BMW-320I",
            "SEDAN", "DIESEL", 190, 2.0, "AUTOMATIC", "RWD", "BLACK", 50000L, true, false);
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        when(carMapper.toResponse(car)).thenReturn(expected);

        var result = service.getCarById(CAR_ID);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getCarByIdThrowsWhenNotFound() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCarById(CAR_ID))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getCarByIdThrowsWhenRemoved() {
        CarEntity car = createCar(CAR_ID, false, false);
        car.setRemoved(true);
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));

        assertThatThrownBy(() -> service.getCarById(CAR_ID))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getCarsReturnsMappedList() {
        CarEntity car = createCar(CAR_ID, true, true);
        CarResponse mapped = new CarResponse(CAR_ID, "BMW", "BMW", "3 Series", "BMW-320I",
            "SEDAN", "DIESEL", 190, 2.0, "AUTOMATIC", "RWD", "BLACK", 50000L, true, true);
        when(carRepository.findAll(any(Specification.class))).thenReturn(List.of(car));
        when(carMapper.toResponse(car)).thenReturn(mapped);

        var result = service.getCars(null, null, null, null, null, null, null,
            null, null, null, null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(CAR_ID);
    }

    @Test
    void getCarAvailabilityReturnsCorrectValues() {
        CarEntity car = createCar(CAR_ID, true, true);
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));

        CarAvailabilityResponse result = service.getCarAvailability(CAR_ID);

        assertThat(result.id()).isEqualTo(CAR_ID);
        assertThat(result.available()).isTrue();
        assertThat(result.testDriveEnabled()).isTrue();
    }

    @Test
    void getCarAvailabilityThrowsWhenNotFound() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCarAvailability(CAR_ID))
            .isInstanceOf(EntityNotFoundException.class);
    }

    private CarEntity createCar(UUID id, boolean available, boolean testDriveEnabled) {
        CarEntity car = new CarEntity();
        car.setId(id);
        car.setAvailable(available);
        car.setTestDriveEnabled(testDriveEnabled);
        car.setRemoved(false);
        car.setBodyType("SEDAN");
        car.setFuelType("DIESEL");
        car.setPowerHp(190);
        car.setEngineLitres(2.0);
        car.setTransmission("AUTOMATIC");
        car.setDrivetrain("RWD");
        car.setColor("BLACK");
        car.setPrice(50000L);
        return car;
    }
}
