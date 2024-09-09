package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductHistoryEntity;
import java.math.BigDecimal;

public class TestProductHistoryFactory {

    public static ProductHistoryEntity createTestProductHistoryEntity(Long id, Integer seq) {
        return ProductHistoryEntity.builder()
                                   .id(id)
                                   .product(ProductEntity.builder()
                                                         .id(seq * 100L)
                                                         .build())
                                   .name("product-%s".formatted(seq))
                                   .description("description-%s".formatted(seq))
                                   .price(BigDecimal.valueOf(seq * 100L))
                                   .category(CategoryEntity.builder().id(-1L).build())
                                   .build();
    }

    public static ProductHistoryEntity createTestProductHistoryEntity(Integer seq) {
        return createTestProductHistoryEntity(null, seq);
    }

}
