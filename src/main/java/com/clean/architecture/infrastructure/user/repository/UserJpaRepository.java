package com.clean.architecture.infrastructure.user.repository;

import com.clean.architecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
