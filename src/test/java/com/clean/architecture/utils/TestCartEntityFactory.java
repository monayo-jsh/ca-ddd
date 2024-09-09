package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;

public class TestCartEntityFactory {

    public static CartEntity createTestCartEntity(Long id, UserEntity userEntity) {
        return CartEntity.builder()
                         .id(id)
                         .user(userEntity)
                         .build();
    }

    public static CartEntity createTestCartEntity(UserEntity userEntity) {
        return createTestCartEntity(null, userEntity);
    }

}
