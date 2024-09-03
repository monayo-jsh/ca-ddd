package com.clean.architecture.infrastructure.user.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("유저 도메인 모델 <> 엔티티 매핑")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("to 엔티티")
    void testToUserEntity() {

        // Given
        User user = User.createNew("user-mapping",
                                   "test",
                                   "test@gmail.com",
                                   "01012345678");

        // When
        UserEntity userEntity = userMapper.toEntity(user);

        // Then
        assertThat(userEntity).isNotNull();

        // 신규 생성 상태
        assertThat(userEntity.getId()).isNull();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userEntity.getStatusChangedAt()).isNotNull();

        // 매핑 확인
        assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
        assertThat(userEntity.getPassword()).isEqualTo(user.getPassword());
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        assertThat(userEntity.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    @DisplayName("to 도메인 모델")
    void testToUser() {

        // Given
        UserEntity userEntity = new UserEntity(
            -1L,
            "user-mapping",
            "test",
            "test@gmail.com",
            "01012345678",
            UserStatus.DELETED,
            LocalDateTime.now()
        );

        // When
        User user = userMapper.toDomain(userEntity);

        // Then
        assertThat(user).isNotNull();

        // 매핑 확인
        assertThat(user.getId()).isEqualTo(userEntity.getId());
        assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(user.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(userEntity.getPhoneNumber());
        assertThat(user.getStatus()).isEqualTo(userEntity.getStatus());
        assertThat(user.getStatusChangedAt()).isEqualTo(userEntity.getStatusChangedAt());
    }

}