package com.clean.architecture.infrastructure.cart.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserStatus;
import com.clean.architecture.infrastructure.user.persistence.repository.JpaUserRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
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
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.cart.persistence"
})
@DisplayName("장바구니 JPA 테스트")
class CartCoreRepositoryTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private CartCoreRepository cartCoreRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity tempUserEntity;

    @BeforeEach
    public void init() {
        Long userSeq = new Random().nextLong();
        UserEntity userEntity = UserEntity.builder()
                                          .username("username-%s".formatted(userSeq))
                                          .password("encrypt-password")
                                          .email("email@google.com")
                                          .phoneNumber("01012345678")
                                          .status(UserStatus.ACTIVE)
                                          .statusChangedAt(LocalDateTime.now())
                                          .build();

        tempUserEntity = jpaUserRepository.save(userEntity);
    }

    @AfterEach
    public void clean() {
        jpaUserRepository.deleteById(tempUserEntity.getId());
    }

    @Nested
    @DisplayName("장바구니 저장")
    class SaveTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testSaveCart() {
            // Given
            CartEntity cartEntity = CartEntity.builder().user(tempUserEntity).build();

            // When
            CartEntity saveCartEntity = cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            CartEntity foundCartEntity = cartCoreRepository.findById(saveCartEntity.getId()).orElse(null);

            // That
            assertThat(foundCartEntity).isNotNull();

            assertThat(foundCartEntity.getUser()).isNotNull();
            assertThat(foundCartEntity.getUser().getId()).isEqualTo(tempUserEntity.getId());

            assertThat(foundCartEntity.getCartItems()).isEmpty();
        }

        @Test
        @DisplayName("하위 항목 포함")
        void testSaveCartWithItems() {
            // Given
            List<CartItemEntity> cartItemEntities = List.of(
                CartItemEntity.builder().productId(100L).quantity(1).build(),
                CartItemEntity.builder().productId(200L).quantity(2).build(),
                CartItemEntity.builder().productId(300L).quantity(3).build()
            );
            CartEntity cartEntity = CartEntity.builder().user(tempUserEntity).build();
            cartEntity.changeCartItems(cartItemEntities);

            // When
            CartEntity saveCartEntity = cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            CartEntity foundCartEntity = cartCoreRepository.findByUserId(tempUserEntity.getId()).orElse(null);

            assertThat(foundCartEntity).isNotNull();

            assertThat(foundCartEntity.getUser()).isNotNull();
            assertThat(foundCartEntity.getUser().getId()).isEqualTo(cartEntity.getUser().getId());

            assertThat(foundCartEntity.getCartItems().size()).isEqualTo(cartItemEntities.size());
            foundCartEntity.getCartItems().forEach(cartItem -> {
                assertThat(cartItem.getId()).isNotNull();
                assertThat(cartItem.getCart().getId()).isEqualTo(saveCartEntity.getId());
            });

        }
    }

    @Nested
    @DisplayName("장바구니 조회")
    class FindTests {

        @Test
        @DisplayName("장바구니 고유키 null 전달")
        void testBadRequestFindById() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartCoreRepository.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("사용자 아이디 null 전달")
        void testBadRequestFindByUserId() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartCoreRepository.findByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("장바구니 존재 확인 null 전달")
        void testBadRequestExistsById() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartCoreRepository.existsByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("데이터 없음")
        void testNotFoundFindByUserId() {
            // Given

            // When
            Optional<CartEntity> findCart = cartCoreRepository.findByUserId(-999L);

            // Then
            assertThat(findCart).isEmpty();
        }

        @Test
        @DisplayName("조회 성공")
        void testFindByUserId() {

            // Given
            CartEntity cartEntity = CartEntity.builder().user(tempUserEntity).build();
            cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            Optional<CartEntity> findCartEntity = cartCoreRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCartEntity).isPresent();

        }

    }

    @Nested
    @DisplayName("장바구니 삭제")
    class DeleteTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testDeleteById() {

            // Given
            CartEntity cartEntity = CartEntity.builder().user(tempUserEntity).build();
            CartEntity saveCartEntity = cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartCoreRepository.deleteById(saveCartEntity.getId());

            Optional<CartEntity> findCart = cartCoreRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCart).isEmpty();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testDeleteByIdWithCartItem() {

            // Given
            List<CartItemEntity> cartItemEntities = List.of(
                CartItemEntity.builder().productId(100L).quantity(1).build(),
                CartItemEntity.builder().productId(200L).quantity(2).build(),
                CartItemEntity.builder().productId(300L).quantity(3).build()
            );
            CartEntity cartEntity = CartEntity.builder().user(tempUserEntity).build();
            cartEntity.changeCartItems(cartItemEntities);

            CartEntity saveCartEntity = cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartCoreRepository.deleteById(saveCartEntity.getId());

            // Then
            Optional<CartEntity> findCart = cartCoreRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCart).isEmpty();

        }

    }
}