package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.http.StorageServiceClient;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestDriveCatalogServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TestDriveRequestRepository testDriveRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private StorageServiceClient storageServiceClient;

    @InjectMocks
    private TestDriveCatalogService service;

    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final UUID CAR_ID    = UUID.randomUUID();

    @Test
    void scheduleTestDriveSuccess() {
        UserEntity client = createUser(CLIENT_ID, UserRole.CLIENT);
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, true));
        when(currentUserProvider.getCurrentUserId()).thenReturn(CLIENT_ID);
        when(userRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(testDriveRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.schedule(new ScheduleTestDriveRequest(CAR_ID, LocalDateTime.now().plusDays(1)));

        assertThat(result.carId()).isEqualTo(CAR_ID);
    }

    @Test
    void scheduleTestDriveThrowsWhenTestDriveDisabled() {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));

        assertThatThrownBy(() ->
            service.schedule(new ScheduleTestDriveRequest(CAR_ID, LocalDateTime.now().plusDays(1))))
            .isInstanceOf(DomainValidationException.class)
            .hasMessageContaining("disabled");
    }

    @Test
    void scheduleTestDriveThrowsWhenStartsAtInPast() {
        assertThatThrownBy(() ->
            service.schedule(new ScheduleTestDriveRequest(CAR_ID, LocalDateTime.now().minusDays(1))))
            .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(storageServiceClient);
    }

    @Test
    void scheduleTestDriveThrowsWhenCarIdNull() {
        assertThatThrownBy(() ->
            service.schedule(new ScheduleTestDriveRequest(null, LocalDateTime.now().plusDays(1))))
            .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(storageServiceClient);
    }

    private UserEntity createUser(UUID id, UserRole role) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setFullName("Test User");
        u.setRole(role);
        return u;
    }
}
