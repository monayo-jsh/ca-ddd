package com.clean.architecture.presentation.user.dto;

import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;

public record UserResponse (

    Long id,
    String username,
    String email,
    String phoneNumber,
    UserStatus status

) {
}
