package com.clean.architecture.infrastructure.cart.repository;

import com.clean.architecture.domain.cart.entity.Cart;
import com.clean.architecture.domain.cart.entity.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Cart> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        QCart cart = QCart.cart;
        Cart foundCart = queryFactory.select(cart)
                                     .from(cart)
                                     .where(cart.userId.eq(userId))
                                     .fetchOne();

        return Optional.ofNullable(foundCart);
    }
}
