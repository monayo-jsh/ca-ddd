package com.clean.architecture.infrastructure.product.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductImageEntity;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({ JpaAuditingConfig.class, QueryDSLConfig.class })
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.product"
})
@DisplayName("상품 레포지토리 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductCoreRepository productCoreRepository;

    // 테스트 데이터 생성을 위한 인터페이스
    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    // 테스트 데이터 생성을 위한 인터페이스
    @Autowired
    private JpaProductRepository jpaProductRepository;

    // 테스트 데이터 생성을 위한 인터페이스
    @Autowired
    private JpaProductImageRepository jpaProductImageRepository;

    @Autowired
    private EntityManager entityManager;

    private CategoryEntity lastCategoryEntity;

    @BeforeEach
    void setUp() {
        long productSeq = new Random().nextLong();

        CategoryEntity parentCategoryEntity = makeTempCategory("음식", null);
        jpaCategoryRepository.save(parentCategoryEntity);

        lastCategoryEntity = makeTempCategory("치킨", parentCategoryEntity);
        jpaCategoryRepository.save(lastCategoryEntity);


        ProductEntity productEntity = makeTempProduct(productSeq, lastCategoryEntity);
        jpaProductRepository.save(productEntity);

        IntStream.range(0, 3).forEach(seq -> {
            ProductImageEntity productImageEntity = makeTempProductImage(productEntity, seq);
            jpaProductImageRepository.save(productImageEntity);
        });

        // 영속성 컨테이너 초기화
        entityManager.clear();
    }

    private CategoryEntity makeTempCategory(String name, CategoryEntity parentCategory) {
        return CategoryEntity.builder()
                             .name(name)
                             .status(CommonStatus.ACTIVE)
                             .parent(parentCategory)
                             .build();
    }

    private ProductEntity makeTempProduct(Long seq, CategoryEntity categoryEntity) {
        return ProductEntity.builder()
                            .name("product-%s".formatted(seq))
                            .description("description-%s".formatted(seq))
                            .price(BigDecimal.valueOf(100))
                            .stockQuantity(10)
                            .category(categoryEntity)
                            .build();
    }

    private ProductImageEntity makeTempProductImage(ProductEntity productEntity, Integer seq) {
        return ProductImageEntity.builder()
                                 .product(productEntity)
                                 .imageUrl("image-url-%s".formatted(seq))
                                 .altText("image-alt-text-%s".formatted(seq))
                                 .sortOrder(seq)
                                 .status(seq % 2 == 0 ? CommonStatus.ACTIVE : CommonStatus.INACTIVE)
                                 .build();
    }

    @Test
    @DisplayName("조회 - EntityGraph")
    void findAllOfEntityGraph() {
        // given

        // when
        List<ProductEntity> productEntities = productCoreRepository.findAll();

        // then

        // 조회 결과가 존재하는지
        assertThat(productEntities).isNotEmpty();

        for (ProductEntity productEntity : productEntities) {

            // 카테고리 조회 확인
            assertThat(productEntity.getCategory()).isNotNull();
            assertThat(productEntity.getCategory().getId()).isNotNull();

            // 이미지 확인
            assertThat(productEntity.getImages()).isNotEmpty();
        }


    }

    @Test
    @DisplayName("조회 - QueryDSL")
    void findAllOfQueryDSL() {
        // given
        Pageable pageable = Pageable.ofSize(10);

        // when
        List<ProductEntity> productEntities = productCoreRepository.findAll(pageable);

        // then

        // 조회 결과가 존재하는지
        assertThat(productEntities).isNotEmpty();

        for (ProductEntity productEntity : productEntities) {

            // 카테고리 조회 확인
            assertThat(productEntity.getCategory()).isNotNull();
            assertThat(productEntity.getCategory().getId()).isNotNull();

            // 이미지 확인
            assertThat(productEntity.getImages()).isNotEmpty();
            for (ProductImageEntity image : productEntity.getImages()) {
                assertThat(image.getStatus()).isEqualTo(CommonStatus.ACTIVE);
            }
        }

        // 수행 쿼리가 1건인지 확인
    }

    @Test
    @DisplayName("조회 - 카테고리 고유키")
    void findAllByCategory() {
        // given
        Pageable pageable = Pageable.ofSize(10);

        // when
        List<ProductEntity> productEntities = productCoreRepository.findAllByCategoryId(lastCategoryEntity.getId(), pageable);

        // then
        // 조회 결과가 존재하는지
        assertThat(productEntities).isNotEmpty();

        for (ProductEntity productEntity : productEntities) {

            // 카테고리 조회 확인
            assertThat(productEntity.getCategory()).isNotNull();
            assertThat(productEntity.getCategory().getId()).isEqualTo(lastCategoryEntity.getId());

            // 이미지 확인
            assertThat(productEntity.getImages()).isNotEmpty();
            for (ProductImageEntity image : productEntity.getImages()) {
                assertThat(image.getStatus()).isEqualTo(CommonStatus.ACTIVE);
            }
        }

        // 수행 쿼리가 1건인지 확인
    }
}