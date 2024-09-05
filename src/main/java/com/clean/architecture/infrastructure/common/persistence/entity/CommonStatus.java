package com.clean.architecture.infrastructure.common.persistence.entity;

import lombok.Getter;

@Getter
public enum CommonStatus {

    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String name;

    CommonStatus(String name) {
        this.name = name;
    }

}
