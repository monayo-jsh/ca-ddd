package com.clean.architecture.infrastructure.cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.domain.cart.entity.Cart;
import com.clean.architecture.domain.cart.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("장바구니 레포지토리 테스트")
class CartCoreRepositoryTest {

    @Autowired
    private CartCoreRepository cartCoreRepository;

    @Test
    @DisplayName("장바구니 저장 - 장바구니만")
    void testSaveCart() {
        // Given
        Cart cart = new Cart(0L);

        // When
        Cart saveCart = cartCoreRepository.save(cart);

        // That
        assertThat(saveCart).isNotNull();
        assertThat(saveCart.getUserId()).isEqualTo(cart.getUserId());
        assertThat(saveCart.getCartItems()).isEmpty();

        assertThat(saveCart.getCreatedAt()).isNotNull();
        assertThat(saveCart.getUpdatedAt()).isNotNull();

    }

    @Test
    @DisplayName("장바구니 저장 - 하위 항목 포함")
    void testSaveCartWithItems() {

        // Given
        List<CartItem> cartItems = List.of(
            new CartItem(1L, 1),
            new CartItem(2L, 2),
            new CartItem(3L, 3)
        );

        Cart cart = new Cart(0L, cartItems);

        // When
        cartCoreRepository.save(cart);
        Cart foundCart = cartCoreRepository.findByUserId(cart.getUserId()).orElse(null);

        assertThat(foundCart).isNotNull();

        assertThat(foundCart.getCartItems().size()).isEqualTo(cartItems.size());

    }

    @Test
    @DisplayName("사용자 장바구니 조회")
    void testFindByUserId() {

        // Given
        Cart cart = new Cart(0L);
        cartCoreRepository.save(cart);

        // When
        Optional<Cart> findCart = cartCoreRepository.findByUserId(cart.getUserId());

        assertThat(findCart).isPresent();

    }

    @Test
    @DisplayName("장바구니 삭제 - 장바구니만")
    void testDeleteById() {

        // Given
        Cart cart = new Cart(0L);
        cartCoreRepository.save(cart);

        // When
        cartCoreRepository.deleteById(cart.getId());

        Optional<Cart> findCart = cartCoreRepository.findByUserId(0L);

        assertThat(findCart).isEmpty();

    }

    @Test
    @DisplayName("장바구니 삭제 - 하위 항목 포함")
    void testDeleteByIdWithCartItem() {

        // Given
        List<CartItem> cartItems = List.of(
            new CartItem(1L, 1),
            new CartItem(2L, 2),
            new CartItem(3L, 3)
        );

        Cart cart = new Cart(0L, cartItems);
        cartCoreRepository.save(cart);

        // When
        cartCoreRepository.deleteById(cart.getId());

        Optional<Cart> findCart = cartCoreRepository.findByUserId(0L);

        assertThat(findCart).isEmpty();

    }
}