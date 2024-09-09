package com.clean.architecture.domain.order.repository;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    List<OrderEntity> findAllByUserId(Long userId, Pageable pageable);

}
