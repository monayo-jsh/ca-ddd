package com.clean.architecture.domain.cart.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

@Getter @Setter
public class Cart {

    @Comment("장바구니 고유키")
    private Long id;

    @Comment("사용자 참조")
    private Long userId;

    @Comment("장바구니 이름")
    private String name;

    @Comment("장바구니 항목 목록")
    private final List<CartItem> cartItems = new ArrayList<>();

    public Cart() {}

    private Cart(Long id, Long userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = initCartName(name);
    }

    private String initCartName(String name) {
        if (StringUtils.hasText(name)) return name;
        return "기본";
    }

    public static Cart create(Long id, Long userId, String name, List<CartItem> cartItems) {
        Cart cart = new Cart(id, userId, name);
        cart.addItems(cartItems);
        return cart;
    }

    public static Cart createNew(Long userId, List<CartItem> cartItems) {
        return create(null, userId, null, cartItems);
    }

    // 비즈니스 로직 - 아이템 단건 삭제
    public void removeItem(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    // 비즈니스 로직 - 아이템 단건 추가
    public void addItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    // 비즈니스 로직 - 아이템 다건 추가
    public void addItems(List<CartItem> cartItems) {
        cartItems.forEach(this::addItem);
    }

    // 비지니스 로직 - 전체 주문 수량 구하기
    public double calculateTotalQuantity() {
        return cartItems.stream()
                        .mapToDouble(CartItem::getQuantity)
                        .sum();
    }

}
