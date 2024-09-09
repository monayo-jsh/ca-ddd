package com.clean.architecture.infrastructure.shipment.persistence.entity;

import com.clean.architecture.infrastructure.common.persistence.entity.AddressEntity;
import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
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
    @Embedded // 값 객체(Value Object) 엔티티의 컬럼으로 관리되어짐
    private AddressEntity address;

    @Builder
    private ShipmentEntity(Long id, Long orderId, String carrier, String trackingNumber, AddressEntity address) {
        this.id = id;
        this.orderId = orderId;
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.address = address;
    }
}
