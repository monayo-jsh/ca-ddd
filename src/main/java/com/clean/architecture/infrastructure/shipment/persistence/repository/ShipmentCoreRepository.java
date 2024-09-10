package com.clean.architecture.infrastructure.shipment.persistence.repository;

import com.clean.architecture.domain.shipment.repository.ShipmentRepository;
import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShipmentCoreRepository implements ShipmentRepository {

    private final JpaShipmentRepository jpaShipmentRepository;

    @Override
    public Optional<ShipmentEntity> findByOrderId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId can not be null");
        }

        return jpaShipmentRepository.findByOrderId(orderId);
    }
}
