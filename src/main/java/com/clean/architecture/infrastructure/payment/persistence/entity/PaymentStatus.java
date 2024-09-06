package com.clean.architecture.infrastructure.payment.persistence.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PENDING("결제 준비"),
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    REFUND("환불");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

}
