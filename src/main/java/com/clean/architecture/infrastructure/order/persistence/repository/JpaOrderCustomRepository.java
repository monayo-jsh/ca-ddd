package com.clean.architecture.infrastructure.order.persistence.repository;

import static com.clean.architecture.infrastructure.order.persistence.entity.QOrderEntity.orderEntity;
import static com.clean.architecture.infrastructure.user.persistence.entity.QUserEntity.userEntity;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaOrderCustomRepository {

    private final JPAQueryFactory queryFactory;


    public Optional<OrderEntity> findById(Long orderId) {
        OrderEntity entity = queryFactory.select(orderEntity)
                                        .from(orderEntity)
                                         .join(orderEntity.user, userEntity).fetchJoin()
                                        .where(orderEntity.id.eq(orderId))
                                        .fetchOne();

        return Optional.ofNullable(entity);
    }
}
