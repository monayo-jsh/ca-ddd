package com.clean.architecture.infrastructure.cart.mapper;

import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-02T14:32:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItem toDomain(CartItemEntity source) {
        if ( source == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        cartItem.setCartId( sourceCartId( source ) );
        cartItem.setId( source.getId() );
        cartItem.setProductId( source.getProductId() );
        cartItem.setQuantity( source.getQuantity() );

        return cartItem;
    }

    @Override
    public CartItemEntity toEntity(CartItem source) {
        if ( source == null ) {
            return null;
        }

        CartItemEntity cartItemEntity = new CartItemEntity();

        cartItemEntity.setId( source.getId() );
        cartItemEntity.setProductId( source.getProductId() );
        cartItemEntity.setQuantity( source.getQuantity() );

        return cartItemEntity;
    }

    private Long sourceCartId(CartItemEntity cartItemEntity) {
        if ( cartItemEntity == null ) {
            return null;
        }
        CartEntity cart = cartItemEntity.getCart();
        if ( cart == null ) {
            return null;
        }
        Long id = cart.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
