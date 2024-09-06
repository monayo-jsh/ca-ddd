package com.clean.architecture.infrastructure.shipment.persistence.entity;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_shipment")
@Comment("배송 테이블")
public class ShipmentEntity extends BaseEntity {

    @Comment("배송 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문 고유키(참조)")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

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
