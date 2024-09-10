package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentMethod;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentPartEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class TestPaymentPartEntityFactory {

    public static PaymentPartEntity createPaymentPartEntity(Long id, Long seq) {
        return PaymentPartEntity.builder()
                                .id(id)
                                .method(PaymentMethod.CREDIT_CARD)
                                .amount(new BigDecimal(seq).setScale(2, RoundingMode.UNNECESSARY)
                                                           .multiply(new BigDecimal(100)))
                                .paidAt(LocalDateTime.now())
                                .build();
    }

    public static PaymentPartEntity createPaymentPartEntity(Long seq) {
        return createPaymentPartEntity(null, seq);
    }
}
