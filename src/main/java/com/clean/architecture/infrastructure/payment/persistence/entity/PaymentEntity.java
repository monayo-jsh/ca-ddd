package com.clean.architecture.infrastructure.payment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Entity
@Table(name = "tb_payment")
@Comment("결제 테이블")
public class PaymentEntity {

    @Comment("결제 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문 고유키")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Comment("총 결제 금액")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Comment("결제 상태")
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // 결제 분할 정보는 결제에서 라이프 사이클을 관리함.
    @Comment("결제 분할 정보")
    @OneToMany(fetch = LAZY, mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentPartEntity> parts = new ArrayList<>();

    @Builder
    private PaymentEntity(Long id, Long orderId, BigDecimal totalAmount, PaymentStatus status, List<PaymentPartEntity> parts) {
        this.id = id;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.parts = parts;
    }

}
