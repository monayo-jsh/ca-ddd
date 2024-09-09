package com.clean.architecture.infrastructure.order.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @NoArgsConstructor
@Entity
@Table(name = "tb_order")
@EntityListeners(AuditingEntityListener.class)
@Comment("주문 테이블")
public class OrderEntity {

    @Comment("주문 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 단순 조회용
    @Comment("사용자 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Comment("주문일시")
    @Column(name = "ordered_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime orderedAt;

    @Comment("주문상태")
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Comment("주문 총 가격")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Comment("수정일시")
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 주문 테이블에서 주문 항목의 라이프사이클을 관리함.
    @Comment("주문 항목 목록")
    @OneToMany(fetch = LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Builder
    private OrderEntity(Long id, UserEntity user, LocalDateTime orderedAt, OrderStatus status, BigDecimal totalAmount, List<OrderItemEntity> items, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.orderedAt = orderedAt;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
        this.updatedAt = updatedAt;
    }

}
