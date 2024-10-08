package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Entity
@Table(
    name = "tb_product",
    indexes = {
        @Index(name = "idx_product_category_id", columnList = "category_id")
    }
)
@Comment("상품 테이블")
public class ProductEntity extends BaseEntity {

    @Comment("상품 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("상품 이름")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Comment("상품 설명")
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Comment("상품 가격")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Comment("상품 재고 수량")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    // 조회만 제공 - 수정메서드 제공 X
    @Comment("상품 카테고리 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CategoryEntity category;

    // API 에서는 조회만 제공해야함 -> cascade 옵션 제거
    // 상품 이미지의 주인은 이미지 테이블이지만, 상품이 있어야 이미지도 있기에 상품 테이블에서 라이프 사이클을 관리함
    @Comment("상품 이미지 목록")
    @OneToMany(fetch = LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> images = new ArrayList<>();

    @Builder
    private ProductEntity(Long id, String name, String description, BigDecimal price, Integer stockQuantity, CategoryEntity category, List<ProductImageEntity> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.images = images;
    }
}
