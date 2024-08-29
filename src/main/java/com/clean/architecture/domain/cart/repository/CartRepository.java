package com.clean.architecture.domain.cart.repository;

import com.clean.architecture.domain.cart.entity.Cart;
import java.util.Optional;

public interface CartRepository {

    Cart save(Cart cart);
    Optional<Cart> findByUserId(Long userId);
    void deleteById(Long id);

}
