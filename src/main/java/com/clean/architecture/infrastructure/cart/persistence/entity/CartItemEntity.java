package com.clean.architecture.infrastructure.cart.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.product.persistence.entity.ProductEntity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
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

    // 단순 조회용
    @Comment("장바구니 고유키(참조)")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 조건은 부여하지 않음
    private CartEntity cart;

    // 단순 조회용
    @Comment("상품 고유키(참조)")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 조건은 부여하지 않음
    private ProductEntity product;

    @Comment("선택된 상품 수량")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Builder
    private CartItemEntity(Long id, CartEntity cart, ProductEntity product, int quantity) {
        this.id = id;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItemEntity changeCart(CartEntity cartEntity) {
        this.cart = cartEntity;
        return this;
    }

}
