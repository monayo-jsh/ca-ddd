package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCartItemEntityFactory {

    public static CartItemEntity createTestCartItemEntity(Integer seq) {
        CategoryEntity categoryEntity = TestCategoryEntityFactory.createTestCategoryEntity("product-category-%s".formatted(seq), null);
        ProductEntity productEntity = TestProductEntityFactory.createTestProductEntity(seq * 100L, (long) seq, categoryEntity);

        return CartItemEntity.builder()
                             .product(productEntity)
                             .quantity(1)
                             .build();
    }

    public static List<CartItemEntity> createTestCartItemEntities(int size) {
        int generateSize = size + 1;
        return IntStream.range(1, generateSize)
                        .mapToObj(TestCartItemEntityFactory::createTestCartItemEntity)
                        .collect(Collectors.toList());
    }
}
