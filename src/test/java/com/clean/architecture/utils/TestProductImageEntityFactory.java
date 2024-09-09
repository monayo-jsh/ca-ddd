package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductImageEntity;

public class TestProductImageEntityFactory {

    public static ProductImageEntity createTestProductImageEntity(Long id, Integer seq, ProductEntity productEntity) {
        return ProductImageEntity.builder()
                                 .id(id)
                                 .product(productEntity)
                                 .imageUrl("url-%s".formatted(seq))
                                 .altText("alt-text-%s".formatted(seq))
                                 .sortOrder(seq)
                                 .status(seq % 2 == 0 ? CommonStatus.ACTIVE : CommonStatus.INACTIVE)
                                 .build();
    }

    public static ProductImageEntity createTestProductImageEntity(Integer seq, ProductEntity productEntity) {
        return createTestProductImageEntity(null, seq, productEntity);
    }
}
