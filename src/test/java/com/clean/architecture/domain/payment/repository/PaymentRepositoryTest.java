package com.clean.architecture.domain.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.order.persistence.entity.OrderEntity;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentEntity;
import com.clean.architecture.infrastructure.payment.persistence.entity.PaymentPartEntity;
import com.clean.architecture.infrastructure.payment.persistence.repository.JpaPaymentRepository;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.utils.TestOrderEntityFactory;
import com.clean.architecture.utils.TestPaymentEntityFactory;
import com.clean.architecture.utils.TestPaymentPartEntityFactory;
import com.clean.architecture.utils.TestUserEntityFactory;
import jakarta.persistence.EntityManager;
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
    "com.clean.architecture.infrastructure.payment"
})
@DisplayName("결제 레포지토리 테스트")
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    private OrderEntity testOrderEntity;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = TestUserEntityFactory.createTestUser(new Random().nextLong());
        testOrderEntity = TestOrderEntityFactory.createTestOrderEntity(new Random().nextLong(), userEntity);
    }

    @Nested
    @DisplayName("결제 정보 조회")
    class testSearch {

        @Test
        @DisplayName("주문 아이디로 조회")
        void findByOrderId() {
            // given
            PaymentPartEntity testPaymentPartEntity = TestPaymentPartEntityFactory.createPaymentPartEntity(1L);
            PaymentEntity testPaymentEntity = TestPaymentEntityFactory.createTestPaymentEntity(testOrderEntity);

            // 연관관계 설정
            testPaymentEntity.addPaymentPart(testPaymentPartEntity);

            jpaPaymentRepository.save(testPaymentEntity);

            entityManager.clear();

            // when
            PaymentEntity foundPaymentEntity = paymentRepository.findByOrderId(testOrderEntity.getId()).orElse(null);

            // then
            assertThat(foundPaymentEntity).isNotNull();
            assertThat(foundPaymentEntity).isNotEqualTo(testPaymentEntity);

            // 지연 로딩 확인
            assertThat(foundPaymentEntity.getOrder()).isInstanceOf(HibernateProxy.class);

            // 데이터 상태 확인
            assertThat(foundPaymentEntity.getTotalAmount()).isEqualTo(testPaymentEntity.getTotalAmount());
            assertThat(foundPaymentEntity.getStatus()).isEqualTo(testPaymentEntity.getStatus());

            assertThat(foundPaymentEntity.getParts()).isNotEmpty();

            PaymentPartEntity foundPaymentPartEntity = foundPaymentEntity.getParts().get(0);

            // 지연 로딩 확인
            assertThat(foundPaymentPartEntity.getPayment()).isEqualTo(foundPaymentEntity);

            assertThat(foundPaymentPartEntity.getMethod()).isEqualTo(testPaymentPartEntity.getMethod());
            assertThat(foundPaymentPartEntity.getAmount()).isEqualTo(testPaymentPartEntity.getAmount());
            assertThat(foundPaymentPartEntity.getPaidAt()).isEqualTo(testPaymentPartEntity.getPaidAt());
        }
    }

}