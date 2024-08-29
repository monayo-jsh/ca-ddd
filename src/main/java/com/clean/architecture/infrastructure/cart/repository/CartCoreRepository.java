package com.clean.architecture.infrastructure.cart.repository;

import com.clean.architecture.domain.cart.entity.Cart;
import com.clean.architecture.domain.cart.repository.CartRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartCoreRepository implements CartRepository {

    private final CartJpaRepository cartJpaRepository;
    private final CartCustomRepository cartCustomRepository;

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartCustomRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        cartJpaRepository.deleteById(id);
    }
}
