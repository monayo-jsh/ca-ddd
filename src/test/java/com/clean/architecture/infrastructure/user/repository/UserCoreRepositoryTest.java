package com.clean.architecture.infrastructure.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.domain.user.entity.User;
import com.clean.architecture.domain.user.entity.UserStatus;
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
    basePackages = "com.clean.architecture.infrastructure.user.repository"
)
@DisplayName("사용자 JPA 테스트")
class UserCoreRepositoryTest {

    @Autowired
    private UserCoreRepository userCoreRepository;

    private User testUser;

    @BeforeEach
    public void init() {
        Long userSeq = new Random().nextLong();

        testUser = new User("username-%s".formatted(userSeq),
                            "encrypt-password",
                            "email@google.com",
                            "01012345678",
                            UserStatus.ACTIVE);
    }

    @Nested
    @DisplayName("사용자 저장")
    class SaveTests {

        @Test
        @DisplayName("저장 성공")
        void testSaveUser() {

            // Given, // When
            User saveUser = userCoreRepository.save(testUser);

            // Then
            assertThat(saveUser.getId()).isNotNull();

            assertThat(saveUser.getStatusChangedAt()).isNotNull();
            assertThat(saveUser.getCreatedAt()).isNotNull();
            assertThat(saveUser.getUpdatedAt()).isNotNull();
        }

    }

    @Nested
    @DisplayName("사용자 조회")
    class FindTests {

        @Test
        @DisplayName("이메일 조회")
        void findByEmail() {

            // Given
            userCoreRepository.save(testUser);

            // When
            Optional<User> foundUser = userCoreRepository.findByEmail(testUser.getEmail());

            // Then
            assertThat(foundUser).isPresent();

            User user = foundUser.get();
            assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
            assertThat(user.getStatus()).isEqualTo(testUser.getStatus());

        }
    }

    @Nested
    @DisplayName("사용자 수정")
    class UpdateTests {

        @Test
        @DisplayName("사용자 상태 변경 처리")
        void testUpdateUser() {

            UserStatus toUserStatus = UserStatus.INACTIVE;
            User saveUser = userCoreRepository.save(testUser);
            saveUser.changeStatus(toUserStatus);
            userCoreRepository.save(saveUser);

            User foundUser = userCoreRepository.findByEmail(testUser.getEmail()).orElse(null);

            // 삭제되지 않음 확인
            assertThat(foundUser).isNotNull();

            // 상태, 변경일시 저장 확인
            assertThat(foundUser.getStatus()).isEqualTo(toUserStatus);
            assertThat(foundUser.getStatusChangedAt()).isEqualTo(saveUser.getStatusChangedAt());
        }
    }
}