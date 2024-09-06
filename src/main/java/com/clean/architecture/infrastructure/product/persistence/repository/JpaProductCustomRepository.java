package com.clean.architecture.infrastructure.product.persistence.repository;

import static com.clean.architecture.infrastructure.product.persistence.entity.QCategoryEntity.categoryEntity;
import static com.clean.architecture.infrastructure.product.persistence.entity.QProductEntity.productEntity;
import static com.clean.architecture.infrastructure.product.persistence.entity.QProductImageEntity.productImageEntity;

import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    private JPAQuery<ProductEntity> createBaseQuery() {
        return queryFactory.select(productEntity)
                           .from(productEntity)
                           .join(productEntity.category, categoryEntity).fetchJoin()
                           .leftJoin(categoryEntity.parent).fetchJoin() // 상위 카테고리
                           .leftJoin(categoryEntity.parent.parent).fetchJoin() // 상위 카테고리의 부모까지
                           .leftJoin(productEntity.images, productImageEntity).fetchJoin();
    }

    private BooleanBuilder createBasicCondition() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(productImageEntity.status.eq(CommonStatus.ACTIVE));
        return builder;
    }

    private JPAQuery<ProductEntity> settingPageable(JPAQuery<ProductEntity> baseQuery, Pageable pageable) {
        if (pageable == null) return baseQuery;

        return baseQuery.offset(pageable.getOffset())
                        .limit(pageable.getPageSize());
    }

    public List<ProductEntity> findAll(Pageable pageable) {
        JPAQuery<ProductEntity> baseQuery = createBaseQuery();
        BooleanBuilder condition = createBasicCondition();

        baseQuery = settingPageable(baseQuery, pageable);

        return baseQuery.where(condition)
                        .orderBy(productEntity.createdAt.desc())
                        .fetch();
    }

    public List<ProductEntity> findAllByCategoryId(Long categoryId, Pageable pageable) {
        JPAQuery<ProductEntity> baseQuery = createBaseQuery();

        BooleanBuilder condition = createBasicCondition();
        condition.and(productEntity.category.id.eq(categoryId));

        baseQuery = settingPageable(baseQuery, pageable);

        return baseQuery.where(condition)
                        .fetch();
    }

}
