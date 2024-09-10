package com.clean.architecture.domain.shipment.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.domain.shipment.repository.ShipmentRepository;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.order.persistence.repository.JpaOrderRepository;
import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentEntity;
import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentStatus;
import com.clean.architecture.infrastructure.shipment.persistence.entity.ShipmentStatusEntity;
import com.clean.architecture.infrastructure.shipment.persistence.repository.JpaShipmentRepository;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.utils.TestOrderEntityFactory;
import com.clean.architecture.utils.TestShipmentEntityFactory;
import com.clean.architecture.utils.TestUserEntityFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ JpaAuditingConfig.class, QueryDSLConfig.class })
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.shipment"
})
@DisplayName("배송 레포지토리 테스트")
class ShipmentRepositoryTest {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JpaShipmentRepository jpaShipmentRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    private OrderEntity testOrderEntity;

    @BeforeEach
    void setUp() {
        UserEntity tempUserEntity = TestUserEntityFactory.createTestUser(new Random().nextLong());
        OrderEntity tempOrderEntity = TestOrderEntityFactory.createTestOrderEntity(tempUserEntity);
        testOrderEntity = jpaOrderRepository.save(tempOrderEntity);

        List<ShipmentStatusEntity> shipmentStatusEntities = List.of(
            ShipmentStatusEntity.builder().status(ShipmentStatus.PENDING).build()
        );
        ShipmentEntity testShipmentEntity = TestShipmentEntityFactory.createTestShipmentEntity(testOrderEntity);
        testShipmentEntity.addStatuses(shipmentStatusEntities);

        jpaShipmentRepository.save(testShipmentEntity);

        entityManager.clear();
    }

    @Nested
    @DisplayName("배송 조회")
    class testSearch {

        @Test
        @DisplayName("주문아이디로 조회")
        void testFindByOrderId() {
            // given

            // when
            ShipmentEntity shipmentEntity = shipmentRepository.findByOrderId(testOrderEntity.getId()).orElse(null);

            // then
            assertThat(shipmentEntity).isNotNull();

            assertThat(shipmentEntity.getAddress()).isNotNull();
            assertThat(shipmentEntity.getAddress().getRoadAddress()).isNotEmpty();

            // 지연 로딩되었는 가?
            assertThat(shipmentEntity.getOrder()).isInstanceOf(HibernateProxy.class);

            // 배송 상태 목록 조회 쿼리 발생 확인
            // 배송 상태가 설정되었는가 ?
            assertThat(shipmentEntity.getStatuses()).isNotEmpty();
            assertThat(shipmentEntity.getStatuses().stream().anyMatch(status -> Objects.equals(status.getStatus(), ShipmentStatus.PENDING))).isTrue();
        }
    }

    @Nested
    @DisplayName("배송 수정")
    class testUpdate {

        @Test
        @DisplayName("배송 상태 추가")
        void testAddShipmentStatus() {
            // given

            // when
            ShipmentEntity updateShipmentEntity = shipmentRepository.findByOrderId(testOrderEntity.getId()).orElse(null);
            assertThat(updateShipmentEntity).isNotNull();

            updateShipmentEntity.addStatus(ShipmentStatusEntity.builder().status(ShipmentStatus.SHIPPED).build()); // 배송중 상태 추가

            jpaShipmentRepository.save(updateShipmentEntity);

            entityManager.flush();
            entityManager.clear();

            // then
            ShipmentEntity shipmentEntity = shipmentRepository.findByOrderId(testOrderEntity.getId()).orElse(null);

            assertThat(shipmentEntity).isNotNull();

            // 배송 상태 목록 조회 쿼리 발생 확인
            // 배송 상태가 추가되었는가 ?
            assertThat(shipmentEntity.getStatuses()).isNotEmpty();
            assertThat(shipmentEntity.getStatuses().stream().anyMatch(status -> Objects.equals(status.getStatus(), ShipmentStatus.SHIPPED))).isTrue();

        }
    }
}