package com.clean.architecture.domain.cart.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.common.model.entity.BaseEntity;
import com.clean.architecture.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@ToString
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
    @Column(name = "id")
    private Long id;

    @Comment("사용자 고유키(참조)")
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

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

    public Cart(User user) {
        this.user = user;
    }

    public Cart(User user, List<CartItem> cartItems) {
        this(user);
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
