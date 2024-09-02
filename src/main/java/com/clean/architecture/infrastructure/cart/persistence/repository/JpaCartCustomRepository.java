package com.clean.architecture.infrastructure.cart.persistence.repository;

import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.QCartEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCartCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CartEntity findByUserId(Long userId) {
        QCartEntity cart = QCartEntity.cartEntity;

        return queryFactory.select(cart)
                           .from(cart)
                           .where(cart.user.id.eq(userId))
                           .fetchOne();
    }
}
