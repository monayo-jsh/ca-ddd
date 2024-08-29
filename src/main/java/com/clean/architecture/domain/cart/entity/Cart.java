package com.clean.architecture.domain.cart.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.common.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(
    name = "tb_cart",
    indexes = {
        @Index(name = "idx_cart_user_id", columnList = "user_id")
    }
)
@Comment("장바구니 테이블")
public class Cart extends BaseEntity {

    @Comment("장바구니 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("사용자 고유키(참조)")
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId; // User 도메인에 대한 참조 대신 userId 로 느슨한 결합

    @Comment("장바구니 이름")
    @Column(name = "name", nullable = false, length = 50)
    @ColumnDefault("'기본'")
    private String name;

    @Comment("장바구니 항목 목록")
    @OneToMany(fetch = LAZY, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.name == null) {
            this.name = "기본";
        }
    }

    protected Cart() {}

    public Cart(Long userId) {
        this.userId = userId;
    }

    public Cart(Long userId, List<CartItem> cartItems) {
        this(userId);
        changeCartItems(cartItems);
    }

    public void changeCartItem(CartItem cartItem) {
        cartItem.changeCart(this);
        this.cartItems.add(cartItem);
    }

    public void changeCartItems(List<CartItem> cartItems) {
        cartItems.forEach(this::changeCartItem);
    }
}
