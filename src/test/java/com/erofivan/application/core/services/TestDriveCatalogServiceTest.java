package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.domain.models.TestDriveRequestEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.TestDriveRequestRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.ScheduleTestDriveRequest;
import com.erofivan.presentation.dtos.responses.TestDriveResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestDriveCatalogServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TestDriveRequestRepository testDriveRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private TestDriveCatalogService service;

    @Test
    void scheduleCreatesTestDrive() {
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.now().plusDays(1);

        UserEntity client = new UserEntity();
        client.setId(clientId);
        client.setFullName("Client");

        CarEntity car = createCar(carId, true);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(testDriveRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TestDriveResponse result = service.schedule(new ScheduleTestDriveRequest(carId, startsAt));

        assertEquals(clientId, result.clientId());
        assertEquals(carId, result.carId());
        assertEquals(startsAt, result.startsAt());
        verify(testDriveRepository).save(any(TestDriveRequestEntity.class));
    }

    @Test
    void scheduleThrowsWhenCarIdNull() {
        assertThrows(DomainValidationException.class,
            () -> service.schedule(new ScheduleTestDriveRequest(null, LocalDateTime.now().plusDays(1))));
    }

    @Test
    void scheduleThrowsWhenStartsAtNull() {
        assertThrows(DomainValidationException.class,
            () -> service.schedule(new ScheduleTestDriveRequest(UUID.randomUUID(), null)));
    }

    @Test
    void scheduleThrowsWhenStartsAtInPast() {
        assertThrows(DomainValidationException.class,
            () -> service.schedule(new ScheduleTestDriveRequest(UUID.randomUUID(), LocalDateTime.now().minusDays(1))));
    }

    @Test
    void scheduleThrowsWhenTestDriveDisabled() {
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();

        UserEntity client = new UserEntity();
        client.setId(clientId);

        CarEntity car = createCar(carId, false);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        assertThrows(DomainValidationException.class,
            () -> service.schedule(new ScheduleTestDriveRequest(carId, LocalDateTime.now().plusDays(1))));
    }

    @Test
    void scheduleThrowsWhenCarNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();

        UserEntity client = new UserEntity();
        client.setId(clientId);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> service.schedule(new ScheduleTestDriveRequest(carId, LocalDateTime.now().plusDays(1))));
    }

    @Test
    void getTestDrivesReturnsMappedList() {
        UUID clientId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.now().plusDays(1);

        UserEntity client = new UserEntity();
        client.setId(clientId);

        CarEntity car = createCar(carId, true);

        TestDriveRequestEntity entity = new TestDriveRequestEntity();
        entity.setId(UUID.randomUUID());
        entity.setClient(client);
        entity.setCar(car);
        entity.setStartsAt(startsAt);

        when(testDriveRepository.findAllBy()).thenReturn(List.of(entity));

        List<TestDriveResponse> result = service.getTestDrives();

        assertEquals(1, result.size());
        assertEquals(clientId, result.get(0).clientId());
        assertEquals(carId, result.get(0).carId());
    }

    private CarEntity createCar(UUID id, boolean testDriveEnabled) {
        CarEntity car = new CarEntity();
        car.setId(id);
        car.setTestDriveEnabled(testDriveEnabled);
        car.setModel(new ModelEntity());
        return car;
    }
}
