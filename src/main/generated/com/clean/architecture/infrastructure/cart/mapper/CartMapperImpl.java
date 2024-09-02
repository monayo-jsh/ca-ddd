package com.clean.architecture.infrastructure.cart.mapper;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-02T14:32:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class CartMapperImpl implements CartMapper {

    private final CartItemMapper cartItemMapper = CartItemMapper.INSTANCE;

    @Override
    public Cart toDomain(CartEntity source) {
        if ( source == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setUserId( sourceUserId( source ) );
        cart.setId( source.getId() );
        cart.setName( source.getName() );
        if ( cart.getCartItems() != null ) {
            List<CartItem> list = cartItemEntityListToCartItemList( source.getCartItems() );
            if ( list != null ) {
                cart.getCartItems().addAll( list );
            }
        }

        return cart;
    }

    @Override
    public CartEntity toEntity(Cart source) {
        if ( source == null ) {
            return null;
        }

        CartEntity cartEntity = new CartEntity();

        cartEntity.setId( source.getId() );
        cartEntity.setName( source.getName() );

        return cartEntity;
    }

    private Long sourceUserId(CartEntity cartEntity) {
        if ( cartEntity == null ) {
            return null;
        }
        UserEntity user = cartEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<CartItem> cartItemEntityListToCartItemList(List<CartItemEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<CartItem> list1 = new ArrayList<CartItem>( list.size() );
        for ( CartItemEntity cartItemEntity : list ) {
            list1.add( cartItemMapper.toDomain( cartItemEntity ) );
        }

        return list1;
    }
}
