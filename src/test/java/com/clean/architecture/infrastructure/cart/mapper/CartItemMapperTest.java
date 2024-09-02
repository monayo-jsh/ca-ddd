package com.clean.architecture.infrastructure.cart.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("카트 항목 도메인 모델 <> 엔티티 매핑")
class CartItemMapperTest {

    @Test
    @DisplayName("to 도메인 (연관 관계 없는 경우)")
    void testToDomain() {

        // Given
        CartItemEntity cartItemEntity = new CartItemEntity(1L, 1L, 1);

        // Then
        CartItem cartItem = CartItemMapper.INSTANCE.toDomain(cartItemEntity);

        // When
        assertThat(cartItem).isNotNull();

        assertThat(cartItem.getId()).isEqualTo(cartItemEntity.getId());
        assertThat(cartItem.getProductId()).isEqualTo(cartItemEntity.getProductId());
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemEntity.getQuantity());

    }

    @Test
    @DisplayName("to 도메인 (연관 관계 있는 경우)")
    void testToDomainWithCart() {

        // Given
        CartEntity cartEntity = new CartEntity(1L, null);
        CartItemEntity cartItemEntity = new CartItemEntity(100L, 1L, 1);
        cartItemEntity.changeCart(cartEntity);

        // Then
        CartItem cartItem = CartItemMapper.INSTANCE.toDomain(cartItemEntity);

        // When
        assertThat(cartItem).isNotNull();

        assertThat(cartItem.getId()).isEqualTo(cartItemEntity.getId());
        assertThat(cartItem.getProductId()).isEqualTo(cartItemEntity.getProductId());
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemEntity.getQuantity());

        assertThat(cartItem.getCartId()).isNotNull();
        assertThat(cartItem.getCartId()).isEqualTo(cartEntity.getId());

    }

    @Test
    @DisplayName("to 엔티티 (연관 관계 없는 경우)")
    void testToEntity() {
        // Given
        CartItem cartItem = CartItem.create(100L, 1000L, 1);

        // Then
        CartItemEntity cartItemEntity = CartItemMapper.INSTANCE.toEntity(cartItem);

        // When
        assertThat(cartItemEntity).isNotNull();

        assertThat(cartItemEntity.getId()).isEqualTo(cartItem.getId());
        assertThat(cartItemEntity.getProductId()).isEqualTo(cartItem.getProductId());
        assertThat(cartItemEntity.getQuantity()).isEqualTo(cartItem.getQuantity());

        assertThat(cartItemEntity.getCart()).isNull();
    }

    @Test
    @DisplayName("to 엔티티 (연관 관계 있는 경우")
    void testToEntityWithCart() {
        // Given
        Cart cart = Cart.create(1L, null, null, Collections.emptyList());
        CartItem cartItem = CartItem.create(100L, cart.getId(), 2L, 2);

        // Then
        CartItemEntity cartItemEntity = CartItemMapper.INSTANCE.toEntity(cartItem);

        // When
        assertThat(cartItemEntity).isNotNull();

        assertThat(cartItemEntity.getId()).isEqualTo(cartItem.getId());
        assertThat(cartItemEntity.getProductId()).isEqualTo(cartItem.getProductId());
        assertThat(cartItemEntity.getQuantity()).isEqualTo(cartItem.getQuantity());

        assertThat(cartItemEntity.getCart()).isNull();
    }

}