package com.clean.architecture.infrastructure.cart.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Entity
@Table(
    name = "tb_cart",
    indexes = {
        @Index(name = "idx_cart_user_id", columnList = "user_id")
    }
)
@Comment("장바구니 테이블")
public class CartEntity extends BaseEntity {

    @Comment("장바구니 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 단순 조회용
    @Comment("사용자 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Comment("장바구니 이름")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // 장바구니 항목은 장바구니에서 라이프사이클을 관리함.
    @Comment("장바구니 항목 목록")
    @OneToMany(fetch = LAZY, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.name == null) {
            this.name = "기본";
        }
    }

    @Builder
    private CartEntity(Long id, UserEntity user, String name, List<CartItemEntity> cartItems) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.cartItems = cartItems;
    }

    // 기본값 설정을 위한 빌더 객체
    public static class CartEntityBuilder {
        private List<CartItemEntity> cartItems = new ArrayList<>();
    }

    public void changeCartItem(CartItemEntity cartItem) {
        cartItem.changeCart(this);
        this.cartItems.add(cartItem);
    }

    public void changeCartItems(List<CartItemEntity> cartItems) {
        cartItems.forEach(this::changeCartItem);
    }
}