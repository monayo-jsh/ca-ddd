package com.clean.architecture.domain.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderItemEntity;
import com.clean.architecture.infrastructure.order.persistence.repository.JpaOrderRepository;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.utils.TestOrderEntityFactory;
import com.clean.architecture.utils.TestOrderItemEntityFactory;
import com.clean.architecture.utils.TestUserEntityFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({ JpaAuditingConfig.class, QueryDSLConfig.class})
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.order"
})
@DisplayName("주문 인터페이스 테스트")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Autowired
    private EntityManager entityManager;

    private final UserEntity tempUserEntity = TestUserEntityFactory.createTestUser();

    @Nested
    @DisplayName("주문 조회")
    class testSearch {

        @Test
        @DisplayName("주문 이력 조회 - 사용자 아이디")
        void testFindByUserId() {
            // given
            List<OrderItemEntity> orderItemEntities = TestOrderItemEntityFactory.createTestOrderItemEntities(3);
            OrderEntity orderEntity = TestOrderEntityFactory.createTestOrderEntity(tempUserEntity);

            // 주문 항목 설정
            orderEntity.addOrderItems(orderItemEntities);

            // 테스트 주문 데이터 생성
            jpaOrderRepository.save(orderEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // when
            Pageable pageable = Pageable.ofSize(10);
            List<OrderEntity> foundOrderEntities = orderRepository.findAllByUserId(tempUserEntity.getId(), pageable);

            // then
            assertThat(foundOrderEntities).isNotEmpty();

            OrderEntity foundOrderEntity = foundOrderEntities.get(0);

            // 매핑 검증
            assertThat(foundOrderEntity.getTotalAmount()).isEqualTo(orderEntity.getTotalAmount());
            assertThat(foundOrderEntity.getStatus()).isEqualTo(orderEntity.getStatus());

            // OneToMany 관계, Fetch = LAZY 상태이기 떄문에 .size() 호출 시점에 쿼리 발생 확인
            // 아이템에 항상 접근한다면 QueryDSL 로 fetch join 이 성능 유리 및 N+1 문제 해결
            assertThat(foundOrderEntity.getItems().size()).isEqualTo(orderEntity.getItems().size());
        }

    }

}