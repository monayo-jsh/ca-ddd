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
    public CartEntity save(CartEntity cartEntity) {
        return jpaCartRepository.save(cartEntity);
    }

    @Override
    public Optional<CartEntity> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        CartEntity foundCartEntity = jpaCartCustomRepository.findByUserId(userId);
        return Optional.ofNullable(foundCartEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaCartRepository.deleteById(id);
    }
}
