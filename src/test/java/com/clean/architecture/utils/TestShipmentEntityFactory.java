package com.clean.architecture.utils;

import com.clean.architecture.infrastructure.common.persistence.entity.AddressEntity;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentEntity;

public class TestShipmentEntityFactory {

    public static ShipmentEntity createTestShipmentEntity(Long id, OrderEntity orderEntity) {
        return ShipmentEntity.builder()
                             .id(id)
                             .order(orderEntity)
                             .carrier("배송 업체명")
                             .trackingNumber("운송장번호")
                             .address(AddressEntity.builder()
                                                   .zipCode("12345")
                                                   .roadAddress("도로명주소")
                                                   .detailAddress("상세주소")
                                                   .build())
                             .build();
    }

    public static ShipmentEntity createTestShipmentEntity(OrderEntity orderEntity) {
        return createTestShipmentEntity(null, orderEntity);
    }

}
