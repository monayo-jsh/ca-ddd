package com.clean.architecture.infrastructure.user.persistence.dto;

import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserStatusUpdateRequest {

    private Long id;
    private UserStatus status;
    private LocalDateTime statusChangedAt;

}
