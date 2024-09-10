package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentEntity;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentStatus;

public class TestPaymentEntityFactory {

    public static PaymentEntity createTestPaymentEntity(Long id, OrderEntity orderEntity) {
        return PaymentEntity.builder()
                            .id(id)
                            .order(orderEntity)
                            .status(PaymentStatus.PENDING)
                            .build();
    }

    public static PaymentEntity createTestPaymentEntity(OrderEntity orderEntity) {
        return createTestPaymentEntity(null, orderEntity);
    }

}
