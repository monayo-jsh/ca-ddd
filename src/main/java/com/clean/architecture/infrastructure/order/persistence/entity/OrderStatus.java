package com.clean.architecture.infrastructure.order.persistence.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("주문 접수"),
    COMPLETED("주문 완료"),
    CANCELED("주문 취소"),
    FAILED("주문 실패");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

}
