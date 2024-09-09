package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import java.time.LocalDateTime;
import java.util.Random;

public class TestUserEntityFactory {

    public static UserEntity createTestUser() {
        Long userSeq = new Random().nextLong();

        return UserEntity.builder()
                         .id(userSeq)
                         .username("username-%s".formatted(userSeq))
                         .password("encrypt-password")
                         .email("email@google.com")
                         .phoneNumber("01012345678")
                         .status(UserStatus.ACTIVE)
                         .statusChangedAt(LocalDateTime.now())
                         .build();
    }

}
