package com.clean.architecture.infrastructure.product.persistence.repository;

import com.clean.architecture.infrastructure.product.persistence.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // 상위 카테고리, 상위 카테고리의 부모까지 즉시 로딩
    // 결국 left outer join 이 실행되는 구조이기 때문에 특정 조건을 부여할 수는 없다.
    // 가져온 뒤 애플리케이션 계층에서 처리가 필요함.
    @EntityGraph(attributePaths = { "parent", "parent.parent" })
    Optional<CategoryEntity> findById(Long id);

    @Query(
        // 네이티브 쿼리 재귀적 쿼리 후 반환
        // 조회 대상을 기준으로 오름차순으로 목록이 구성되어 반환됨
        nativeQuery = true,
        value =
            "WITH RECURSIVE category_hierarchy(id, name, parent_category_id, status, created_at, updated_at) AS (" +
            "    SELECT id, name, parent_category_id, status, created_at, updated_at " +
            "    FROM tb_category " +
            "    WHERE id = :id " +
            "    AND status = 'ACTIVE' " +
            "    UNION ALL " +
            "    SELECT c.id, c.name, c.parent_category_id, c.status, c.created_at, c.updated_at " +
            "    FROM tb_category c " +
            "    INNER JOIN category_hierarchy ch ON ch.parent_category_id = c.id " +
            "    WHERE c.status = 'ACTIVE' " +
            ") " +
            "SELECT * FROM category_hierarchy"
    )
    List<CategoryEntity> findCategoryHierarchyById(Long id);

}
