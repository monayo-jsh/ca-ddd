package com.clean.architecture.domain.product.repository;

import com.clean.architecture.infrastructure.product.persistence.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
