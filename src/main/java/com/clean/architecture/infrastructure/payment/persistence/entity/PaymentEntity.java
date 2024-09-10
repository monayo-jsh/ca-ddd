package com.clean.architecture.infrastructure.payment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // 단순 참조용
    @Comment("주문 고유키")
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 제약조건 부여하지 않음
    private OrderEntity order;

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
    private PaymentEntity(Long id, OrderEntity order, BigDecimal totalAmount, PaymentStatus status, List<PaymentPartEntity> parts) {
        this.id = id;
        this.order = order;
        this.totalAmount = totalAmount;
        this.status = status;
        this.parts = parts;
    }

    // 초기화 설정을 위한 빌더 객체
    public static class PaymentEntityBuilder {

        private BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        private List<PaymentPartEntity> parts = new ArrayList<>();

    }

    public void changeStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public void addPaymentPart(PaymentPartEntity paymentPartEntity) {
        this.parts.add(paymentPartEntity);
        paymentPartEntity.changePayment(this);
    }

    public void addPaymentParts(List<PaymentPartEntity> paymentPartEntities) {
        paymentPartEntities.forEach(this::addPaymentPart);
    }

}
