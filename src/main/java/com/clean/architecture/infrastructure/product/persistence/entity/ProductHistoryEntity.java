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
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@NoArgsConstructor
@Comment("상품 이력 테이블")
@Entity
@Table(name = "tb_product_history")
@EntityListeners(AuditingEntityListener.class)
public class ProductHistoryEntity {

    @Comment("상품 이력 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

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

    @Comment("상품 이미지 이력 목록")
    @OneToMany(fetch = LAZY, mappedBy = "productHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageHistoryEntity> images;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

}
