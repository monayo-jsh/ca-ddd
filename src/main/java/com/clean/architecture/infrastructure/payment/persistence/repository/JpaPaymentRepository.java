package com.clean.architecture.infrastructure.payment.persistence.repository;

import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByOrderId(Long orderId);

}
