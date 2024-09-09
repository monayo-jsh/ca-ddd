package com.clean.architecture.domain.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
import com.clean.architecture.infrastructure.product.persistence.entity.ProductImageEntity;
import com.clean.architecture.infrastructure.product.persistence.repository.JpaCategoryRepository;
import com.clean.architecture.infrastructure.product.persistence.repository.JpaProductImageRepository;
import com.clean.architecture.infrastructure.product.persistence.repository.JpaProductRepository;
import com.clean.architecture.utils.TestCategoryEntityFactory;
import com.clean.architecture.utils.TestProductEntityFactory;
import com.clean.architecture.utils.TestProductImageEntityFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    private ProductRepository productRepository;

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

        CategoryEntity parentCategoryEntity = TestCategoryEntityFactory.createTestCategoryEntity("음식", null);
        jpaCategoryRepository.save(parentCategoryEntity);

        lastCategoryEntity = TestCategoryEntityFactory.createTestCategoryEntity("치킨", parentCategoryEntity);
        jpaCategoryRepository.save(lastCategoryEntity);


        ProductEntity productEntity = TestProductEntityFactory.createTestProductEntity(productSeq, lastCategoryEntity);
        jpaProductRepository.save(productEntity);

        IntStream.range(0, 3).forEach(seq -> {
            ProductImageEntity productImageEntity = TestProductImageEntityFactory.createTestProductImageEntity(seq, productEntity);
            jpaProductImageRepository.save(productImageEntity);
        });

        // 영속성 컨테이너 초기화
        entityManager.clear();
    }

    @Nested
    @DisplayName("상품 조회")
    class testSearch {

        @Test
        @DisplayName("EntityGraph 활용 조회")
        void findAllOfEntityGraph() {
            // given

            // when
            List<ProductEntity> productEntities = productRepository.findAll();

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
        @DisplayName("QueryDSL 조회")
        void findAllOfQueryDSL() {
            // given
            Pageable pageable = Pageable.ofSize(10);

            // when
            List<ProductEntity> productEntities = productRepository.findAll(pageable);

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
        @DisplayName("카테고리 고유키로 조회")
        void findAllByCategory() {
            // given
            Pageable pageable = Pageable.ofSize(10);

            // when
            List<ProductEntity> productEntities = productRepository.findAllByCategoryId(lastCategoryEntity.getId(), pageable);

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

    @Nested
    @DisplayName("상품 삭제")
    class testDelete {

        @Test
        @DisplayName("상품 엔티티에서 이미지 삭제")
        void testDeleteProductImage() {
            // given
            Pageable pageable = Pageable.ofSize(1);

            // when
            List<ProductEntity> productEntities = productRepository.findAllByCategoryId(lastCategoryEntity.getId(), pageable);

            // then
            // 조회 결과가 존재하는지
            assertThat(productEntities).isNotEmpty();

            ProductEntity productEntity = productEntities.get(0);
            assertThat(productEntity.getImages()).isNotEmpty();

            productEntity.getImages().remove(0);

            // 영속성 컨테이너 초기화
            entityManager.flush();
            entityManager.clear();

            List<ProductEntity> foundProductEntities = productRepository.findAllByCategoryId(lastCategoryEntity.getId(), pageable);
            assertThat(foundProductEntities).isNotEmpty();

            ProductEntity foundProductEntity = foundProductEntities.get(0);

            // 이미지가 삭제 되었어야 함.
            assertThat(productEntity.getImages().size()).isEqualTo(foundProductEntity.getImages().size());
        }

        @Test
        @DisplayName("상품 이미지 엔티티에서 이미지 삭제")
        void testNotDeletedProductImage() {
            // given
            Pageable pageable = Pageable.ofSize(1);

            // when
            List<ProductEntity> productEntities = productRepository.findAllByCategoryId(lastCategoryEntity.getId(), pageable);

            // then
            // 조회 결과가 존재하는지
            assertThat(productEntities).isNotEmpty();

            ProductEntity productEntity = productEntities.get(0);
            assertThat(productEntity.getImages()).isNotEmpty();

            ProductImageEntity productImageEntity = productEntity.getImages().get(0);
            // 연관관계 끊어내기 위함 null 설정 시 하이버네이트에서 not-null property references a null or transient value 를 발생시킴
            // 따라서 없는 상품 정보로 설정해서 업데이트 쿼리 나가는지 확인 -> 업데이트 쿼리 발생
            // 결과적으로 영속성 컨테이너에 등록되면서 JPA 에서 이 객체를 관리하므로 상태 변화를 감지 후 업데이트 쿼리를 발생 시킴
            // 조회로만 사용하려면
            //  1. DTO 로 조회해서 수정 메서드를 제공하지 않는 방법,
            //  2. 또는 엔티티에서 조회만 제공한다면 해당 엔티티에서는 수정 메서드를 제거해야 함. changeProduct 메서드를 삭제해야함.
            //productImageEntity.changeProduct(ProductEntity.builder().id(-999L).build());
        }
    }
}