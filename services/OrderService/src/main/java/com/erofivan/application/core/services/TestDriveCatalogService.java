package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.application.contracts.services.TestDriveService;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.TestDriveRequestEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.http.StorageServiceClient;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import com.erofivan.presentation.dtos.responses.TestDriveResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestDriveCatalogService implements TestDriveService {
    private final UserRepository userRepository;
    private final TestDriveRequestRepository testDriveRepository;
    private final CurrentUserProvider currentUserProvider;
    private final StorageServiceClient storageServiceClient;

    @Transactional(readOnly = true)
    public List<TestDriveResponse> getTestDrives() {
        return testDriveRepository.findAllBy().stream()
            .map(entity -> new TestDriveResponse(
                entity.getId(), entity.getClient().getId(),
                entity.getCarId(), entity.getStartsAt()))
            .toList();
    }

    @Transactional
    public TestDriveResponse schedule(@NonNull ScheduleTestDriveRequest request) {
        if (request.carId() == null)
            throw new DomainValidationException("carId is required");

        if (request.startsAt() == null)
            throw new DomainValidationException("startsAt is required");

        if (request.startsAt().isBefore(LocalDateTime.now()))
            throw new DomainValidationException("startsAt must be in the future");

        CarAvailabilityResponse car = storageServiceClient.getCarAvailability(request.carId());

        if (!car.testDriveEnabled()) {
            throw new DomainValidationException("Test drive is disabled for this car");
        }

        UUID clientId = currentUserProvider.getCurrentUserId();

        UserEntity client = userRepository.findById(clientId)
            .filter(u -> !u.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Client", clientId.toString()));

        TestDriveRequestEntity entity = new TestDriveRequestEntity();
        entity.setClient(client);
        entity.setCarId(request.carId());
        entity.setStartsAt(request.startsAt());
        testDriveRepository.save(entity);

        return new TestDriveResponse(entity.getId(), client.getId(), entity.getCarId(), entity.getStartsAt());
    }
}
