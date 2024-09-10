package com.clean.architecture.infrastructure.shipment.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @NoArgsConstructor
@Entity
@Table(name = "tb_shipment_status")
@EntityListeners(AuditingEntityListener.class)
@Comment("배송 상태 테이블")
public class ShipmentStatusEntity {

    @Comment("배송 상태 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("배송 고유키(참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shipment_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ShipmentEntity shipment;

    @Comment("배송 상태")
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @Comment("생성일시")
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    private ShipmentStatusEntity(Long id, ShipmentEntity shipment, ShipmentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.shipment = shipment;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void changeShipment(ShipmentEntity shipmentEntity) {
        this.shipment = shipmentEntity;
    }
}
