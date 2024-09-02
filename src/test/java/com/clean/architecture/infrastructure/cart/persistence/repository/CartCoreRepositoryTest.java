package com.clean.architecture.infrastructure.cart.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
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
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.cart.persistence.repository"
})
@DisplayName("장바구니 JPA 테스트")
class CartCoreRepositoryTest {

    @Autowired
    private CartCoreRepository cartCoreRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity testUserEntity;

    @BeforeEach
    public void init() {
        testUserEntity = UserEntity.createTest(new Random().nextLong());
    }

    @Nested
    @DisplayName("장바구니 저장")
    class SaveTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testSaveCart() {
            // Given
            CartEntity cartEntity = new CartEntity(testUserEntity);

            // When
            CartEntity saveCartEntity = cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // That
            assertThat(saveCartEntity).isNotNull();

            assertThat(saveCartEntity.getId()).isNotNull();

            assertThat(saveCartEntity.getUser()).isNotNull();
            assertThat(saveCartEntity.getUser().getId()).isEqualTo(testUserEntity.getId());

            assertThat(saveCartEntity.getCartItems()).isEmpty();

            assertThat(saveCartEntity.getCreatedAt()).isNotNull();
            assertThat(saveCartEntity.getUpdatedAt()).isNotNull();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testSaveCartWithItems() {

            // Given
            List<CartItemEntity> cartItemEntities = List.of(
                new CartItemEntity(1L, 1),
                new CartItemEntity(2L, 2),
                new CartItemEntity(3L, 3)
            );

            CartEntity cartEntity = new CartEntity(testUserEntity, cartItemEntities);

            // When
            cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            CartEntity foundCartEntity = cartCoreRepository.findByUserId(testUserEntity.getId()).orElse(null);

            assertThat(foundCartEntity).isNotNull();
            assertThat(foundCartEntity.getId()).isNotNull();

            assertThat(foundCartEntity.getUser()).isNotNull();
            assertThat(foundCartEntity.getUser().getId()).isEqualTo(cartEntity.getUser().getId());

            assertThat(foundCartEntity.getCartItems().size()).isEqualTo(cartItemEntities.size());
            foundCartEntity.getCartItems().forEach(cartItem -> {
                assertThat(cartItem.getId()).isNotNull();
                assertThat(cartItem.getCart().getId()).isEqualTo(cartEntity.getId());
            });

        }
    }

    @Nested
    @DisplayName("장바구니 조회")
    class FindTests {

        @Test
        @DisplayName("사용자 아이디 null 전달")
        void testBadRequestFindByUserId() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartCoreRepository.findByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("데이터 없음")
        void testNotFoundFindByUserId() {
            // Given

            // When
            Optional<CartEntity> findCartEntity = cartCoreRepository.findByUserId(-999L);

            // Then
            assertThat(findCartEntity).isEmpty();
        }

        @Test
        @DisplayName("조회 성공")
        void testFindByUserId() {

            // Given
            CartEntity cartEntity = new CartEntity(testUserEntity);
            cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            Optional<CartEntity> findCartEntity = cartCoreRepository.findByUserId(testUserEntity.getId());

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
            CartEntity cartEntity = new CartEntity(testUserEntity);
            cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartCoreRepository.deleteById(cartEntity.getId());

            Optional<CartEntity> findCartEntity = cartCoreRepository.findByUserId(testUserEntity.getId());

            assertThat(findCartEntity).isEmpty();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testDeleteByIdWithCartItem() {

            // Given
            List<CartItemEntity> cartItemEntities = List.of(
                new CartItemEntity(1L, 1),
                new CartItemEntity(2L, 2),
                new CartItemEntity(3L, 3)
            );

            CartEntity cartEntity = new CartEntity(testUserEntity, cartItemEntities);
            cartCoreRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartCoreRepository.deleteById(cartEntity.getId());

            Optional<CartEntity> findCartEntity = cartCoreRepository.findByUserId(testUserEntity.getId());

            assertThat(findCartEntity).isEmpty();

        }

    }
}