package com.clean.architecture.infrastructure.product.persistence.repository;

import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    // 이미지와 카테고리, 카테고리의 parent (상위카테고리)만 즉시 로딩
    @EntityGraph(attributePaths = { "images", "category", "category.parent" })
    List<ProductEntity> findAll();

}
