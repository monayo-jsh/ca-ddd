package com.clean.architecture.domain.payment.repository;

import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentEntity;
import java.util.Optional;

public interface PaymentRepository {

    Optional<PaymentEntity> findByOrderId(Long orderId);

}
