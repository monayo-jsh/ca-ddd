package com.clean.architecture.infrastructure.cart.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("카트 항목 도메인 모델 <> 엔티티 매핑")
class CartItemMapperTest {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Test
    @DisplayName("to 도메인 (연관 관계 없는 경우)")
    void testToDomain() {

        // Given
        CartItemEntity cartItemEntity = CartItemEntity.builder().id(100L).product(ProductEntity.builder().id(100L).build()).quantity(1).build();

        // Then
        CartItem cartItem = cartItemMapper.toDomain(cartItemEntity);

        // When
        assertThat(cartItem).isNotNull();

        assertThat(cartItem.getId()).isEqualTo(cartItemEntity.getId());
        assertThat(cartItem.getProductId()).isEqualTo(cartItemEntity.getProduct().getId());
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemEntity.getQuantity());

    }

    @Test
    @DisplayName("to 도메인 (연관 관계 있는 경우)")
    void testToDomainWithCart() {

        // Given
        CartEntity cartEntity = CartEntity.builder().id(1L).build();
        CartItemEntity cartItemEntity = CartItemEntity.builder().id(100L).product(ProductEntity.builder().id(100L).build()).quantity(1).build();
        cartItemEntity.changeCart(cartEntity);

        // Then
        CartItem cartItem = cartItemMapper.toDomain(cartItemEntity);

        // When
        assertThat(cartItem).isNotNull();

        assertThat(cartItem.getId()).isEqualTo(cartItemEntity.getId());
        assertThat(cartItem.getProductId()).isEqualTo(cartItemEntity.getProduct().getId());
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemEntity.getQuantity());

        assertThat(cartItem.getCartId()).isNotNull();
        assertThat(cartItem.getCartId()).isEqualTo(cartEntity.getId());

    }

    @Test
    @DisplayName("to 엔티티 (연관 관계 없는 경우)")
    void testToEntity() {
        // Given
        CartItem cartItem = CartItem.create(100L, 100L, 1);

        // Then
        CartItemEntity cartItemEntity = cartItemMapper.toEntity(cartItem);

        // When
        assertThat(cartItemEntity).isNotNull();

        assertThat(cartItemEntity.getId()).isEqualTo(cartItem.getId());
        assertThat(cartItemEntity.getProduct().getId()).isEqualTo(cartItem.getProductId());
        assertThat(cartItemEntity.getQuantity()).isEqualTo(cartItem.getQuantity());

        assertThat(cartItemEntity.getCart()).isNull();
    }

    @Test
    @DisplayName("to 엔티티 (연관 관계 있는 경우)")
    void testToEntityWithCart() {
        // Given
        Cart cart = Cart.create(1L, null, null, Collections.emptyList());
        CartItem cartItem = CartItem.create(100L, cart.getId(), 200L, 2);

        // Then
        CartItemEntity cartItemEntity = cartItemMapper.toEntity(cartItem);

        // When
        assertThat(cartItemEntity).isNotNull();

        assertThat(cartItemEntity.getId()).isEqualTo(cartItem.getId());
        assertThat(cartItemEntity.getProduct().getId()).isEqualTo(cartItem.getProductId());
        assertThat(cartItemEntity.getQuantity()).isEqualTo(cartItem.getQuantity());

        assertThat(cartItemEntity.getCart()).isNull();
    }

}