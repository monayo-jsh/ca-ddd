package com.clean.architecture.domain.shipment.repository;

import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentEntity;
import java.util.Optional;

public interface ShipmentRepository {

    Optional<ShipmentEntity> findByOrderId(Long orderId);

}
