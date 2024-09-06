package com.clean.architecture.infrastructure.order.persistence.entity;

import lombok.Getter;

@Getter
public enum ShipmentStatus {

    PENDING("배송 준비"),
    SHIPPED("배송중"),
    DELIVERED("배송 완료"),
    RETURNED("반송");

    private final String name;

    ShipmentStatus(String name) {
        this.name = name;
    }
}
