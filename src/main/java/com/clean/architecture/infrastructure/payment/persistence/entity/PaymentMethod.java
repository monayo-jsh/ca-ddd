package com.clean.architecture.infrastructure.payment.persistence.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    CREDIT_CARD("신용카드");

    private final String name;

    PaymentMethod(String name) {
        this.name = name;
    }
}
