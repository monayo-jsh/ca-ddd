package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @NoArgsConstructor
@Entity
@Table(name = "tb_product_history")
@EntityListeners(AuditingEntityListener.class)
@Comment("상품 이력 테이블")
public class ProductHistoryEntity {

    @Comment("상품 이력 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 조회만 제공
    @Comment("상품 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;

    @Comment("상품 이름")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Comment("상품 설명")
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Comment("상품 가격")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Comment("상품 카테고리 고유키(참조)")
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CategoryEntity category;

    // API 에서는 조회만 제공해야함.
    // 상품 이미지의 주인은 이미지 테이블이지만, 상품이 있고 이미지가 있기에 상품 엔티티에서 라이프 사이클을 관리함.
    @Comment("상품 이미지 이력 목록")
    @OneToMany(fetch = LAZY, mappedBy = "productHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageHistoryEntity> images = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @Builder
    private ProductHistoryEntity(Long id, ProductEntity product, String name, String description, BigDecimal price, CategoryEntity category, List<ProductImageHistoryEntity> images, LocalDateTime createdAt) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.createdAt = createdAt;
    }
}
