package com.clean.architecture.infrastructure.product.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ JpaAuditingConfig.class, QueryDSLConfig.class })
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.product"
})
@DisplayName("카테고리 조회 JPA 테스트")
class JpaCategoryRepositoryTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("조회 - EntityGraph")
    void findEntityGraph() {

        // given
        CategoryEntity categoryEntity1 = makeTempCategory("한국", null, CommonStatus.ACTIVE);
        jpaCategoryRepository.save(categoryEntity1);

        CategoryEntity categoryEntity2 = makeTempCategory("음식", categoryEntity1, CommonStatus.ACTIVE);
        jpaCategoryRepository.save(categoryEntity2);

        CategoryEntity categoryEntity3 = makeTempCategory("치킨", categoryEntity2, CommonStatus.ACTIVE);
        jpaCategoryRepository.save(categoryEntity3);

        entityManager.clear();

        // when
        CategoryEntity foundCategoryEntity = jpaCategoryRepository.findById(categoryEntity3.getId()).orElse(null);

        // then
        assertThat(foundCategoryEntity).isNotNull();

        assertThat(foundCategoryEntity.getName()).isEqualTo(categoryEntity3.getName());

        assertThat(foundCategoryEntity.getParent()).isNotNull();
        assertThat(foundCategoryEntity.getParent().getName()).isEqualTo(categoryEntity2.getName());

        assertThat(foundCategoryEntity.getParent().getParent()).isNotNull();
        assertThat(foundCategoryEntity.getParent().getParent().getName()).isEqualTo(categoryEntity1.getName());

    }

    @Test
    @DisplayName("조회 - 재귀적 쿼리")
    void findWithRecursive() {

        // given
        CategoryEntity categoryEntity1 = makeTempCategory("한국", null, CommonStatus.ACTIVE);
        jpaCategoryRepository.save(categoryEntity1);

        CategoryEntity categoryEntity2 = makeTempCategory("음식", categoryEntity1, CommonStatus.INACTIVE);
        jpaCategoryRepository.save(categoryEntity2);

        CategoryEntity categoryEntity3 = makeTempCategory("치킨", categoryEntity2, CommonStatus.ACTIVE);
        jpaCategoryRepository.save(categoryEntity3);

        entityManager.clear();

        // when
        List<CategoryEntity> foundCategoryEntities = jpaCategoryRepository.findCategoryHierarchyById(categoryEntity3.getId());

        // then
        assertThat(foundCategoryEntities).isNotEmpty();

        foundCategoryEntities.forEach(entity -> {
            System.out.printf("category: %s, status: %s%n", entity.getName(), entity.getStatus());
        });

        // 쿼리 1번만 수행되었는지 확인
    }

    private CategoryEntity makeTempCategory(String name, CategoryEntity parentCategory, CommonStatus status) {
        return CategoryEntity.builder()
                             .name(name)
                             .status(status)
                             .parent(parentCategory)
                             .build();
    }
}