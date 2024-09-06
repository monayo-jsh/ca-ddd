package com.clean.architecture.infrastructure.payment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Entity
@Table(name = "tb_payment_part")
@Comment("결제 분할 테이블")
public class PaymentPartEntity {

    @Comment("결제 분할 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("결제 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PaymentEntity payment;

    @Comment("분할 결제 방법")
    @Column(name = "method", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Comment("분할 결제 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("분할 결제 일시")
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Builder
    private PaymentPartEntity(Long id, PaymentEntity payment, PaymentMethod method, BigDecimal amount, LocalDateTime paidAt) {
        this.id = id;
        this.payment = payment;
        this.method = method;
        this.amount = amount;
        this.paidAt = paidAt;
    }
}
