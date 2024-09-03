package com.clean.architecture.domain.user.repository;

import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import java.util.Optional;

public interface UserRepository {

    UserEntity save(UserEntity userEntity);
    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

}
