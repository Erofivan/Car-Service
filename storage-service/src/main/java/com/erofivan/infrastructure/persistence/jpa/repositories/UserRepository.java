package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.UserRole;
import com.erofivan.domain.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByIdAndRoleAndRemovedFalse(UUID id, UserRole role);

    List<UserEntity> findByRoleAndRemovedFalse(UserRole role);
}
