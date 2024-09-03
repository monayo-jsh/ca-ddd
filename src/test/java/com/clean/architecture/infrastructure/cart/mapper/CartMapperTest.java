package com.clean.architecture.infrastructure.cart.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("카트 도메인 모델 <> 엔티티 매핑")
class CartMapperTest {

    @Autowired
    private CartMapper cartMapper;

    private UserEntity testUserEntity;

    @BeforeEach
    public void init() {
        testUserEntity = new UserEntity();
        testUserEntity.setId(-999L);
    }


    @Test
    @DisplayName("to 엔티티 (하위 항목 없음)")
    void testToEntity() {

        // Given
        User user = new User(-999L);
        Cart cart = Cart.createNew(user.getId());

        // When
        CartEntity cartEntity = cartMapper.toEntity(cart);

        // Then
        assertThat(cartEntity).isNotNull();

        // 신규 생성 상태
        assertThat(cartEntity.getId()).isNull();
        assertThat(cartEntity.getName()).isEqualTo("기본");

        // 매핑 확인
        assertThat(cartEntity.getUser()).isNotNull();
        assertThat(cartEntity.getUser().getId()).isEqualTo(user.getId());

        assertThat(cartEntity.getCartItems().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("to 엔티티 (하위 항목 있음)")
    void testToEntityWithChild() {

        // Given
        User user = new User(-999L);
        List<CartItem> cartItems = List.of(
            CartItem.create(100L, 1L, 1),
            CartItem.create(200L, 2L, 2),
            CartItem.create(300L, 3L, 3)
        );
        Cart cart = Cart.create(1L, user.getId(), null, cartItems);

        // When
        CartEntity cartEntity = cartMapper.toEntity(cart);

        // Then
        assertThat(cartEntity).isNotNull();

        // 신규 생성 상태
        assertThat(cartEntity.getId()).isNotNull();
        assertThat(cartEntity.getName()).isEqualTo("기본");

        // 매핑 확인
        assertThat(cartEntity.getUser()).isNotNull();
        assertThat(cartEntity.getUser().getId()).isEqualTo(user.getId());

        assertThat(cartEntity.getCartItems().size()).isZero(); // 하위 항목은 비즈니스 로직에서 매핑
    }


    @Test
    @DisplayName("to 도메인 모델 (하위 항목 없음)")
    void testToDomain() {
        // Given
        CartEntity cartEntity = new CartEntity(1L, testUserEntity);

        // When
        Cart cart = cartMapper.toDomain(cartEntity);

        // Then
        assertThat(cart).isNotNull();

        // 매핑 확인
        assertThat(cart.getId()).isEqualTo(cartEntity.getId());
        assertThat(cart.getName()).isEqualTo(cartEntity.getName());

        assertThat(cart.getUserId()).isNotNull();
        assertThat(cart.getUserId()).isEqualTo(cartEntity.getUser().getId());

        assertThat(cart.getCartItems().size()).isEqualTo(cartEntity.getCartItems().size());
    }

    @Test
    @DisplayName("to 도메인 모델 (하위 항목 있음)")
    void testToDomainWithChild() {
        // Given
        CartEntity cartEntity = new CartEntity(1L, testUserEntity);
        List<CartItemEntity> cartItemEntities = List.of(
            new CartItemEntity(100L, 1L, 1),
            new CartItemEntity(200L, 2L, 2),
            new CartItemEntity(300L, 3L, 3)
        );
        cartEntity.changeCartItems(cartItemEntities);

        // When
        Cart cart = cartMapper.toDomain(cartEntity);

        // Then
        assertThat(cart).isNotNull();

        // 매핑 확인
        assertThat(cart.getId()).isEqualTo(cartEntity.getId());
        assertThat(cart.getName()).isEqualTo(cartEntity.getName());

        assertThat(cart.getUserId()).isNotNull();
        assertThat(cart.getUserId()).isEqualTo(cartEntity.getUser().getId());

        assertThat(cart.getCartItems().size()).isEqualTo(cartEntity.getCartItems().size());
        cart.getCartItems().forEach(cartItem -> assertThat(cartItem.getCartId()).isEqualTo(cart.getId()));
    }
}