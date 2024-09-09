package com.clean.architecture.domain.order.repository;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Optional<OrderEntity> findById(Long OrderId);
    List<OrderEntity> findAllByUserId(Long userId, Pageable pageable);

    OrderEntity save(OrderEntity orderEntity);

}
