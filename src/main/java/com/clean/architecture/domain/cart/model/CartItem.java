package com.clean.architecture.domain.cart.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter @Setter
public class CartItem {

    @Comment("장바구니 항목 고유키")
    private Long id;

    @Comment("장바구니 고유키")
    private Long cartId;

    @Comment("상품 고유키(참조)")
    private Long productId;

    @Comment("선택된 상품 수량")
    private int quantity;

    public CartItem() {}

    private CartItem(Long id, Long cartId, Long productId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartItem create(Long id, Long productId, int quantity) {
        return create(id, null, productId, quantity);
    }

    public static CartItem create(Long id, Long cartId, Long productId, int quantity) {
        return new CartItem(id, cartId, productId, quantity);
    }

}
