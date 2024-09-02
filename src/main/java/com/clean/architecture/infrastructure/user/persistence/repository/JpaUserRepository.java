package com.clean.architecture.infrastructure.user.persistence.repository;

import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
}
