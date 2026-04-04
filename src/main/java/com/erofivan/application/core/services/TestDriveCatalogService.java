package com.erofivan.application.core.services;

import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.TestDriveRequestEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.TestDriveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDriveCatalogService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final TestDriveRequestRepository testDriveRepository;

    @Transactional(readOnly = true)
    public List<TestDriveResponse> getTestDrives() {
        return testDriveRepository.findAllBy().stream()
            .map(entity -> new TestDriveResponse(
                entity.getId(), entity.getClient().getId(),
                entity.getCar().getId(), entity.getStartsAt()))
            .toList();
    }

    @Transactional
    public TestDriveResponse schedule(ScheduleTestDriveRequest request) {
        if (request.clientId() == null)
            throw new DomainValidationException("clientId is required");

        if (request.carId() == null)
            throw new DomainValidationException("carId is required");

        if (request.startsAt() == null)
            throw new DomainValidationException("startsAt is required");

        if (request.startsAt().isBefore(LocalDateTime.now()))
            throw new DomainValidationException("startsAt must be in the future");

        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), UserRole.CLIENT)
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isTestDriveEnabled())
            throw new DomainValidationException("Test drive is disabled for this car");

        TestDriveRequestEntity entity = new TestDriveRequestEntity();
        entity.setClient(client);
        entity.setCar(car);
        entity.setStartsAt(request.startsAt());
        testDriveRepository.save(entity);

        return new TestDriveResponse(entity.getId(), client.getId(), car.getId(), entity.getStartsAt());
    }
}
