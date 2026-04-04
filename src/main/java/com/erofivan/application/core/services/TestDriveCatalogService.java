package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.infrastructure.persistence.jpa.model.CarEntity;
import com.erofivan.infrastructure.persistence.jpa.model.TestDriveRequestEntity;
import com.erofivan.infrastructure.persistence.jpa.model.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.TestDriveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDriveCatalogService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final TestDriveRequestRepository testDriveRepository;

    @Transactional
    public TestDriveResponse schedule(ScheduleTestDriveRequest request) {
        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isTestDriveEnabled()) {
            throw new IllegalStateException("Test drive is disabled for this car");
        }

        TestDriveRequestEntity entity = new TestDriveRequestEntity();
        entity.setClient(client);
        entity.setCar(car);
        entity.setStartsAt(request.startsAt());
        testDriveRepository.save(entity);

        return new TestDriveResponse(entity.getId(), client.getId(), car.getId(), entity.getStartsAt());
    }
}
