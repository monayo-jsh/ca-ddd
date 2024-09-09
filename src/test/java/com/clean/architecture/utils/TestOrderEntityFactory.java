package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderStatus;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class TestOrderEntityFactory {

    public static OrderEntity createTestOrderEntity(Long id, UserEntity userEntity) {
        return OrderEntity.builder()
                          .id(id)
                          .user(userEntity)
                          .orderedAt(LocalDateTime.now())
                          .status(OrderStatus.PENDING)
                          .totalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY))
                          .build();
    }

    public static OrderEntity createTestOrderEntity(UserEntity userEntity) {
        return createTestOrderEntity(null, userEntity);
    }

}
