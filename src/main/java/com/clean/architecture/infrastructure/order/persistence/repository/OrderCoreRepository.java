package com.clean.architecture.infrastructure.order.persistence.repository;

import com.clean.architecture.domain.order.repository.OrderRepository;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderCoreRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderCustomRepository jpaOrderCustomRepository;

    @Override
    public Optional<OrderEntity> findById(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId can be not null");
        }

        return jpaOrderCustomRepository.findById(orderId);
    }

    @Override
    public List<OrderEntity> findAllByUserId(Long userId, Pageable pageable) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can be not null");
        }

        return jpaOrderRepository.findByUserId(userId, pageable);
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return jpaOrderRepository.save(orderEntity);
    }

}
