package com.clean.architecture.infrastructure.order.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.product.persistence.entity.ProductHistoryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_order_item")
@Comment("주문 항목 테이블")
public class OrderItemEntity {

    @Comment("주문 항목 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private OrderEntity order;

    // 주문 당시 상품 정보로 연결
    @Comment("상품 이력 고유키")
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "product_history_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProductHistoryEntity product;

    @Comment("주문한 상품 수량")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Builder
    private OrderItemEntity(Long id, OrderEntity order, ProductHistoryEntity product, Integer quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
}
