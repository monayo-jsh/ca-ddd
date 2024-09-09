package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import java.time.LocalDateTime;

public class TestUserEntityFactory {

    public static UserEntity createTestUser(Long seq) {
        return UserEntity.builder()
                         .id(seq)
                         .username("username-%s".formatted(seq == null ? "" : seq))
                         .password("encrypt-password")
                         .email("email@google.com")
                         .phoneNumber("01012345678")
                         .status(UserStatus.ACTIVE)
                         .statusChangedAt(LocalDateTime.now())
                         .build();
    }

    public static UserEntity createTestUser() {
        return createTestUser(null);
    }
}
