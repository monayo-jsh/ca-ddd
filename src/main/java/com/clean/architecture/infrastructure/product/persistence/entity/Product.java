package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_product")
@Comment("상품 테이블")
public class Product extends BaseEntity {

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

    @Comment("상품 카테고리 고유키(참조)")
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @OneToMany(fetch = LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

}
