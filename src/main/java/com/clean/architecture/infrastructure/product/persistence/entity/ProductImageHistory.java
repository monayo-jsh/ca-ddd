package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@NoArgsConstructor
@Comment("상품이미지 이력 테이블")
@Entity
@Table(name = "tb_product_image_history")
@EntityListeners(AuditingEntityListener.class)
public class ProductImageHistory {

    @Comment("상품이미지 이력 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("상품 이력 고유키")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_history_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProductHistory productHistory;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

}
