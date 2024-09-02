package com.clean.architecture.infrastructure.user.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.user.persistence.dto.UserStatusUpdateRequest;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import com.clean.architecture.infrastructure.user.persistence.mapper.UserStatusUpdateRequestMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
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
    basePackages = "com.clean.architecture.infrastructure.user.persistence.repository"
)
@DisplayName("사용자 JPA 테스트")
class UserCoreRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserCoreRepository userCoreRepository;

    private UserEntity testUserEntity;

    @BeforeEach
    public void init() {
        Long userSeq = new Random().nextLong();

        testUserEntity = UserEntity.create("username-%s".formatted(userSeq),
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
            UserEntity saveUserEntity = userCoreRepository.save(testUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // When
            UserEntity foundUserEntity = userCoreRepository.findById(saveUserEntity.getId()).orElse(null);

            // 테스트를 위해 신규 객체 확인
            assertThat(foundUserEntity).isNotEqualTo(saveUserEntity);

            // Then
            assertThat(foundUserEntity).isNotNull();

            assertThat(foundUserEntity.getId()).isNotNull();

            assertThat(foundUserEntity.getStatusChangedAt()).isNotNull();
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
            userCoreRepository.save(testUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // When
            Optional<UserEntity> foundUserEntity = userCoreRepository.findByEmail(testUserEntity.getEmail());

            // Then
            assertThat(foundUserEntity).isPresent();

            UserEntity userEntity = foundUserEntity.get();
            assertThat(userEntity.getEmail()).isEqualTo(testUserEntity.getEmail());
            assertThat(userEntity.getStatus()).isEqualTo(testUserEntity.getStatus());

        }
    }

    @Nested
    @DisplayName("사용자 수정")
    class UpdateTests {

        @Test
        @DisplayName("사용자 상태 변경 처리")
        void testUpdateUserStatus() {

            // Given
            UserStatus toUserStatus = UserStatus.INACTIVE;
            UserEntity saveUserEntity = userCoreRepository.save(testUserEntity);

            // 영속성 초기화해서 조회할 수 있도록 설정
            entityManager.clear();

            // Then
            UserStatusUpdateRequest userStatusUpdateRequest = UserStatusUpdateRequestMapper.INSTANCE.toRequest(saveUserEntity);
            userStatusUpdateRequest.setStatus(toUserStatus);
            userStatusUpdateRequest.setStatusChangedAt(LocalDateTime.now());
            userCoreRepository.updateStatus(userStatusUpdateRequest);

            // When
            UserEntity foundUserEntity = userCoreRepository.findByEmail(testUserEntity.getEmail()).orElse(null);

            // 테스트를 위해 신규 객체 확인
            assertThat(foundUserEntity).isNotNull();
            assertThat(foundUserEntity).isNotEqualTo(saveUserEntity);

            // 삭제되지 않음 확인
            assertThat(foundUserEntity).isNotNull();

            // 상태, 변경일시 저장 확인
            assertThat(foundUserEntity.getStatus()).isEqualTo(userStatusUpdateRequest.getStatus());
            assertThat(foundUserEntity.getStatusChangedAt()).isEqualTo(userStatusUpdateRequest.getStatusChangedAt());
        }
    }

}