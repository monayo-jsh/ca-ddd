package com.clean.architecture.domain.cart.service;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.repository.CartRepository;
import com.clean.architecture.infrastructure.cart.mapper.CartMapper;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public Cart getCartByUserId(Long userId) {
        CartEntity cartEntity = cartRepository.findByUserId(userId)
                                              .orElseThrow(() -> new RuntimeException("사용자 장바구니 없음"));
        return cartMapper.toDomain(cartEntity);
    }
    public Cart createCart(Cart cart) {
        CartEntity cartEntity = cartMapper.toEntity(cart);
        CartEntity saveCartEntity = cartRepository.save(cartEntity);
        return cartMapper.toDomain(saveCartEntity);
    }

    public void validateExists(Long userId) {
        if (cartRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("사용자 장바구니 이미 존재");
        }
    }
}
