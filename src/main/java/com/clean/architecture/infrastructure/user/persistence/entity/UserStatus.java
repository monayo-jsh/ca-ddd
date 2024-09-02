package com.clean.architecture.infrastructure.user.persistence.entity;

import lombok.Getter;

@Getter
public enum UserStatus {

    // 정상적으로 활동 중인 상태
    ACTIVE("정상"),

    // 활동하지 않지만, 계정은 유지중인 상태 (일정 기간 활동 이력이 없을 경우의 상태)
    INACTIVE("비활성화"),

    // 탈퇴한 상태
    DELETED("탈퇴"),

    // 일시적으로 정지된 상태 (규칙 위반 등의 이유로 활동 제한)
    SUSPENDED("정지"),

    // 장기간 비활성화 상태로 인해 자동으로 휴면 처리된 상태
    HUMAN("휴면");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }
}
