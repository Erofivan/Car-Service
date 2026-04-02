package com.erofivan.application.core;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.infrastructure.persistence.jpa.model.CarJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.TestDriveRequestJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.UserJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.erofivan.presentation.dto.ScheduleTestDriveRequest;
import com.erofivan.presentation.dto.TestDriveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDriveCatalogService {
    private final CarJpaRepository carRepository;
    private final UserJpaRepository userRepository;
    private final TestDriveRequestJpaRepository testDriveRepository;

    @Transactional
    public TestDriveResponse schedule(ScheduleTestDriveRequest request) {
        UserJpaEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarJpaEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isTestDriveEnabled()) {
            throw new IllegalStateException("Test drive is disabled for this car");
        }

        TestDriveRequestJpaEntity entity = new TestDriveRequestJpaEntity();
        entity.setClient(client);
        entity.setCar(car);
        entity.setStartsAt(request.startsAt());
        testDriveRepository.save(entity);

        return new TestDriveResponse(entity.getId(), client.getId(), car.getId(), entity.getStartsAt());
    }
}
