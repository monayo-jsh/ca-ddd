package com.clean.architecture.domain.cart.repository;

import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import java.util.Optional;

public interface CartRepository {

    CartEntity save(CartEntity cartEntity);
    Optional<CartEntity> findById(Long id);
    Optional<CartEntity> findByUserId(Long userId);
    void deleteById(Long id);

    boolean existsByUserId(Long userId);
}
