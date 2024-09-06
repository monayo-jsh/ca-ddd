package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_product_image")
@Comment("상품이미지 테이블")
public class ProductImageEntity extends BaseEntity {

    @Comment("상품이미지 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("상품 고유키")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;

    @Comment("이미지 URL")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Comment("이미지 대체 텍스트")
    @Column(name = "alt_text", nullable = true, length = 255)
    private String altText;

    @Comment("이미지 정렬 순서")
    @Column(name = "sort_order", nullable = false)
    @ColumnDefault("1")
    private Integer sortOrder;

    @Comment("이미지 상태")
    @Column(name = "status", nullable = false, length = 20)
    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    @Builder
    private ProductImageEntity(Long id, ProductEntity product, String imageUrl, String altText, Integer sortOrder, CommonStatus status) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.sortOrder = sortOrder;
        this.status = status;
    }
}
