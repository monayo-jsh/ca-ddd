package com.clean.architecture.infrastructure.cart.repository;

import com.clean.architecture.domain.cart.entity.Cart;
import com.clean.architecture.domain.cart.entity.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Cart findByUserId(Long userId) {
        QCart cart = QCart.cart;
        return queryFactory.select(cart)
                           .from(cart)
                           .where(cart.user.id.eq(userId))
                           .fetchOne();
    }
}
