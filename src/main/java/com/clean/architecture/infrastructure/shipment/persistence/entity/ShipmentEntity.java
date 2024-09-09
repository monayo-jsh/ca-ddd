package com.clean.architecture.infrastructure.shipment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.AddressEntity;
import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) // 외래키 설정하지 않음
    private OrderEntity order;

    @Comment("배송 업체")
    @Column(name = "carrier", nullable = false, length = 50)
    private String carrier;

    @Comment("운송장 번호")
    @Column(name = "tracking_number", nullable = false, length = 255)
    private String trackingNumber;

    @Comment("배송 주소")
    @Embedded // 값 객체(Value Object) 엔티티의 컬럼으로 관리되어짐
    private AddressEntity address;

    // 배송 상태 목록은 배송 테이블에서 라이프사이클을 관리함.
    @Comment("배송 상태 목록")
    @OneToMany(fetch = LAZY, mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentStatusEntity> statuses = new ArrayList<>();

    @Builder
    private ShipmentEntity(Long id, OrderEntity order, String carrier, String trackingNumber, AddressEntity address, List<ShipmentStatusEntity> statuses) {
        this.id = id;
        this.order = order;
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.address = address;
        this.statuses = statuses;
    }
}
