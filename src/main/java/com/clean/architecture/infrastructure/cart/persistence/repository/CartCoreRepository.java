package com.clean.architecture.infrastructure.cart.persistence.repository;

import com.clean.architecture.domain.cart.repository.CartRepository;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartCoreRepository implements CartRepository {

    private final JpaCartRepository jpaCartRepository;
    private final JpaCartCustomRepository jpaCartCustomRepository;

    @Override
    public CartEntity save(CartEntity cart) {
        return jpaCartRepository.save(cart);
    }

    @Override
    public Optional<CartEntity> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        CartEntity cartEntity = jpaCartCustomRepository.findByUserId(userId);
        return Optional.ofNullable(cartEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        jpaCartRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        return jpaCartRepository.existsByUserId(userId);
    }
}
