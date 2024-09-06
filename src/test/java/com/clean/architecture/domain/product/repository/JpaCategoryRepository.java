package com.clean.architecture.domain.product.repository;

import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
