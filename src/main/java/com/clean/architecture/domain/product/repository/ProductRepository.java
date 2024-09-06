package com.clean.architecture.domain.product.repository;

import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    List<ProductEntity> findAll();
    List<ProductEntity> findAll(Pageable pageable);
    List<ProductEntity> findAllByCategoryId(Long categoryId, Pageable pageable);

}
