package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;

public class TestCategoryEntityFactory {

    public static CategoryEntity createTestCategoryEntity(String name, CommonStatus status, CategoryEntity parentCategory) {
        return CategoryEntity.builder()
                             .name(name)
                             .status(status)
                             .parent(parentCategory)
                             .build();
    }

    public static CategoryEntity createTestCategoryEntity(String name, CategoryEntity parentCategory) {
        return createTestCategoryEntity(name, CommonStatus.ACTIVE, parentCategory);
    }

}
