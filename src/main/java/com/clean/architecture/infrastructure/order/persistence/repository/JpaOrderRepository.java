package com.clean.architecture.infrastructure.order.persistence.repository;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserId(Long userId, Pageable pageable);

}
