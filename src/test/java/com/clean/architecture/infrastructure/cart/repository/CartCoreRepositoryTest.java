package com.clean.architecture.infrastructure.cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.domain.cart.entity.Cart;
import com.clean.architecture.domain.cart.entity.CartItem;
import com.clean.architecture.domain.user.entity.User;
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
    "com.clean.architecture.infrastructure.cart.repository"
})
@DisplayName("장바구니 JPA 테스트")
class CartCoreRepositoryTest {

    @Autowired
    private CartCoreRepository cartCoreRepository;

    private User testUser;

    @BeforeEach
    public void init() {
        testUser = new User(new Random().nextLong());
    }

    @Nested
    @DisplayName("장바구니 저장")
    class SaveTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testSaveCart() {
            // Given
            Cart cart = new Cart(testUser);

            // When
            Cart saveCart = cartCoreRepository.save(cart);

            // That
            assertThat(saveCart).isNotNull();

            assertThat(saveCart.getUser()).isNotNull();
            assertThat(saveCart.getUser().getId()).isEqualTo(testUser.getId());

            assertThat(saveCart.getCartItems()).isEmpty();

            assertThat(saveCart.getCreatedAt()).isNotNull();
            assertThat(saveCart.getUpdatedAt()).isNotNull();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testSaveCartWithItems() {

            // Given
            List<CartItem> cartItems = List.of(
                new CartItem(1L, 1),
                new CartItem(2L, 2),
                new CartItem(3L, 3)
            );

            Cart cart = new Cart(testUser, cartItems);

            // When
            cartCoreRepository.save(cart);
            Cart foundCart = cartCoreRepository.findByUserId(testUser.getId()).orElse(null);

            assertThat(foundCart).isNotNull();

            assertThat(foundCart.getUser()).isNotNull();
            assertThat(foundCart.getUser().getId()).isEqualTo(cart.getUser().getId());

            assertThat(foundCart.getCartItems().size()).isEqualTo(cartItems.size());

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
            Optional<Cart> findCart = cartCoreRepository.findByUserId(-999L);

            // Then
            assertThat(findCart).isEmpty();
        }

        @Test
        @DisplayName("조회 성공")
        void testFindByUserId() {

            // Given
            Cart cart = new Cart(testUser);
            cartCoreRepository.save(cart);

            // When
            Optional<Cart> findCart = cartCoreRepository.findByUserId(testUser.getId());

            assertThat(findCart).isPresent();

        }

    }

    @Nested
    @DisplayName("장바구니 삭제")
    class DeleteTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testDeleteById() {

            // Given
            Cart cart = new Cart(testUser);
            cartCoreRepository.save(cart);

            // When
            cartCoreRepository.deleteById(cart.getId());

            Optional<Cart> findCart = cartCoreRepository.findByUserId(testUser.getId());

            assertThat(findCart).isEmpty();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testDeleteByIdWithCartItem() {

            // Given
            List<CartItem> cartItems = List.of(
                new CartItem(1L, 1),
                new CartItem(2L, 2),
                new CartItem(3L, 3)
            );

            Cart cart = new Cart(testUser, cartItems);
            cartCoreRepository.save(cart);

            // When
            cartCoreRepository.deleteById(cart.getId());

            Optional<Cart> findCart = cartCoreRepository.findByUserId(testUser.getId());

            assertThat(findCart).isEmpty();

        }

    }
}