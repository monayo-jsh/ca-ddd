package com.clean.architecture.infrastructure.product.persistence.repository;

import com.clean.architecture.domain.product.repository.ProductRepository;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCoreRepository implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final JpaProductCustomRepository jpaProductCustomRepository;

    @Override
    public List<ProductEntity> findAll() {
        return jpaProductRepository.findAll();
    }

    @Override
    public List<ProductEntity> findAll(Pageable pageable) {
        return jpaProductCustomRepository.findAll(pageable);
    }

    @Override
    public List<ProductEntity> findAllByCategoryId(Long categoryId, Pageable pageable) {
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId can not be null");
        }

        return jpaProductCustomRepository.findAllByCategoryId(categoryId, pageable);
    }
}
