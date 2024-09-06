package com.clean.architecture.infrastructure.shipment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_shipment")
@Comment("배송 테이블")
public class ShipmentEntity extends BaseEntity {

    @Comment("배송 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문 고유키(참조)")
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private OrderEntity order;

    @Comment("배송 업체")
    @Column(name = "carrier", nullable = false, length = 50)
    private String carrier;

    @Comment("운송장 번호")
    @Column(name = "tracking_number", nullable = false, length = 255)
    private String trackingNumber;

    @Comment("배송 주소")
    @Column(name = "shipping_address", nullable = false, length = 255)
    private String shippingAddress;

}
