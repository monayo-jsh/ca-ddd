package com.clean.architecture.infrastructure.cart.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter  @Setter
@NoArgsConstructor
@Entity
@Table(
    name = "tb_cart_item"
)
@Comment("장바구니 항목 테이블")
public class CartItemEntity {

    @Comment("장바구니 항목 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("장바구니 고유키(참조)")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 조건은 부여하지 않음
    private CartEntity cart;

    @Comment("상품 고유키(참조)")
    @Column(name = "product_id", nullable = false)
    private Long productId; // 상품 엔티티를 직접 참조하지 않고 느슨한 결합

    @Comment("선택된 상품 수량")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    public CartItemEntity(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItemEntity(Long id, Long productId, int quantity) {
        this(productId, quantity);
        this.id = id;
    }

    public CartItemEntity changeCart(CartEntity cartEntity) {
        this.cart = cartEntity;
        return this;
    }

}
