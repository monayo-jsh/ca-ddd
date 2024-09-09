package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.math.BigDecimal;

public class TestProductEntityFactory {

    public static ProductEntity createTestProductEntity(Long id, Long seq, CategoryEntity categoryEntity) {
        return ProductEntity.builder()
                            .id(id)
                            .name("product-%s".formatted(seq))
                            .description("description-%s".formatted(seq))
                            .price(BigDecimal.valueOf(100))
                            .stockQuantity(10)
                            .category(categoryEntity)
                            .build();

    }

    public static ProductEntity createTestProductEntity(Long seq, CategoryEntity categoryEntity) {
        return createTestProductEntity(null, seq, categoryEntity);
    }
}
