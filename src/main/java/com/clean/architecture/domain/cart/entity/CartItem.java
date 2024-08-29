package com.clean.architecture.domain.cart.entity;

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
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(
    name = "tb_cart_item"
)
@Comment("장바구니 항목 테이블")
public class CartItem {

    @Comment("장바구니 항목 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("장바구니 고유키(참조)")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 조건은 부여하지 않음
    private Cart cart;

    @Comment("상품 고유키(참조)")
    @Column(name = "product_id", nullable = false)
    private Long productId; // 상품 엔티티를 직접 참조하지 않고 느슨한 결합

    @Comment("선택된 상품 수량")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected CartItem() {}

    public CartItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void changeCart(Cart cart) {
        this.cart = cart;
    }
}
