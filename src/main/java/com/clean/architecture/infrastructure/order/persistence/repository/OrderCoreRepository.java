package com.clean.architecture.infrastructure.order.persistence.repository;

import com.clean.architecture.domain.order.repository.OrderRepository;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderCoreRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public List<OrderEntity> findAllByUserId(Long userId, Pageable pageable) {
        return jpaOrderRepository.findByUserId(userId, pageable);
    }

}
