package com.clean.architecture.infrastructure.shipment.persistence.repository;

import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaShipmentRepository extends JpaRepository<ShipmentEntity, Long> {

    Optional<ShipmentEntity> findByOrderId(Long orderId);

}
