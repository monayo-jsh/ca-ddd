package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderItemEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductHistoryEntity;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TestOrderItemEntityFactory {

    public static OrderItemEntity createTestOrderItemEntity(Long id, Integer seq) {
        ProductHistoryEntity productHistoryEntity = TestProductHistoryFactory.createTestProductHistoryEntity(new Random().nextLong(), seq);

        return OrderItemEntity.builder()
                              .id(id)
                              .product(productHistoryEntity)
                              .quantity(seq)
                              .build();
    }

    public static OrderItemEntity createTestOrderItemEntity(Integer seq) {
        return createTestOrderItemEntity(null, seq);
    }

    public static List<OrderItemEntity> createTestOrderItemEntities(int size) {
        int generatedSize = size + 1;
        return IntStream.range(1, generatedSize)
                        .mapToObj(TestOrderItemEntityFactory::createTestOrderItemEntity)
                        .toList();
    }
}
