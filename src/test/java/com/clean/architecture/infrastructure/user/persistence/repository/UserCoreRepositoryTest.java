package com.clean.architecture.infrastructure.user.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import com.clean.architecture.infrastructure.user.persistence.mapper.UserUpdateRequestMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDSLConfig.class})
@ComponentScan(
    basePackages = "com.clean.architecture.infrastructure.user.persistence"
)
@DisplayName("사용자 JPA 테스트")
class UserCoreRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserCoreRepository userCoreRepository;

    @Autowired
    private UserUpdateRequestMapper userUpdateRequestMapper;

    private UserEntity tempUserEntity;

    @BeforeEach
    public void init() {
        Long userSeq = new Random().nextLong();

        tempUserEntity = new UserEntity(null,
                                        "username-%s".formatted(userSeq),
                                        "encrypt-password",
                                        "email@google.com",
                                        "01012345678",
                                        UserStatus.ACTIVE,
                                        LocalDateTime.now());
    }

    @Nested
    @DisplayName("사용자 저장")
    class SaveTests {

        @Test
        @DisplayName("저장 성공")
        void testSaveUser() {
            // Given, // When
            UserEntity saveUser = userCoreRepository.save(tempUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // When
            UserEntity foundUserEntity = userCoreRepository.findById(saveUser.getId()).orElse(null);

            // 테스트를 위해 신규 객체 확인
            assertThat(foundUserEntity).isNotEqualTo(saveUser);

            // Then
            assertThat(foundUserEntity).isNotNull();

            // 저장된 컬럼 확인
            assertThat(foundUserEntity.getUsername()).isEqualTo(saveUser.getUsername());
            assertThat(foundUserEntity.getPassword()).isEqualTo(saveUser.getPassword());
            assertThat(foundUserEntity.getEmail()).isEqualTo(saveUser.getEmail());
            assertThat(foundUserEntity.getPhoneNumber()).isEqualTo(saveUser.getPhoneNumber());
            assertThat(foundUserEntity.getStatus()).isEqualTo(saveUser.getStatus());
            assertThat(foundUserEntity.getStatusChangedAt()).isEqualTo(saveUser.getStatusChangedAt());

            // 자동 주입 필드 확인
            assertThat(foundUserEntity.getCreatedAt()).isNotNull();
            assertThat(foundUserEntity.getUpdatedAt()).isNotNull();
        }

    }

    @Nested
    @DisplayName("사용자 조회")
    class FindTests {

        @Test
        @DisplayName("이메일 조회")
        void findByEmail() {
            // Given
            userCoreRepository.save(tempUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // When
            UserEntity foundUserEntity = userCoreRepository.findByEmail(tempUserEntity.getEmail()).orElse(null);

            // Then
            assertThat(foundUserEntity).isNotNull();

            assertThat(foundUserEntity.getEmail()).isEqualTo(tempUserEntity.getEmail());
        }
    }

    @Nested
    @DisplayName("사용자 수정")
    class UpdateTests {

        @Test
        @DisplayName("사용자 상태 변경 처리")
        void testUpdateUserStatus() {

            // Given
            UserEntity saveUserEntity = userCoreRepository.save(tempUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // Then
            // 실제 사용은 entity -> domain model -> model.changeStatus() -> to entity -> to Request
            UserStatusUpdateRequest userStatusUpdateRequest = userUpdateRequestMapper.toStatusUpdateRequest(saveUserEntity);
            userStatusUpdateRequest.setStatus(UserStatus.INACTIVE);
            userStatusUpdateRequest.setStatusChangedAt(LocalDateTime.now());
            userCoreRepository.updateStatus(userStatusUpdateRequest);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // When
            UserEntity foundUserEntity = userCoreRepository.findByEmail(tempUserEntity.getEmail()).orElse(null);

            assertThat(foundUserEntity).isNotNull();

            // 테스트를 위해 신규 객체 확인
            assertThat(foundUserEntity).isNotEqualTo(saveUserEntity);

            // 상태, 변경일시 저장 확인
            assertThat(foundUserEntity.getStatus()).isEqualTo(userStatusUpdateRequest.getStatus());
            assertThat(foundUserEntity.getStatusChangedAt()).isEqualTo(userStatusUpdateRequest.getStatusChangedAt());
        }
    }

}