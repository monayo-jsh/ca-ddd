package com.clean.architecture.infrastructure.payment.persistence.repository;

import com.clean.architecture.domain.payment.repository.PaymentRepository;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentCoreRepository implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    @Override
    public Optional<PaymentEntity> findByOrderId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId can not be null");
        }

        return jpaPaymentRepository.findByOrderId(orderId);
    }

}
