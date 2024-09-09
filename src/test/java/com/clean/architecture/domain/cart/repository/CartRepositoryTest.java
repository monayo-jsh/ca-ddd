package com.clean.architecture.domain.cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clean.architecture.config.JpaAuditingConfig;
import com.clean.architecture.config.QueryDSLConfig;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import com.clean.architecture.infrastructure.user.persistence.entity.UserEntity;
import com.clean.architecture.infrastructure.user.persistence.repository.JpaUserRepository;
import com.clean.architecture.utils.TestCartEntityFactory;
import com.clean.architecture.utils.TestCartItemEntityFactory;
import com.clean.architecture.utils.TestUserEntityFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDSLConfig.class})
@ComponentScan(basePackages = {
    "com.clean.architecture.infrastructure.cart.persistence"
})
@DisplayName("장바구니 레포지토리 테스트")
class CartRepositoryTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity tempUserEntity;

    @BeforeEach
    public void init() {
        UserEntity testUser = TestUserEntityFactory.createTestUser();
        tempUserEntity = jpaUserRepository.save(testUser);
    }

    @AfterEach
    public void clean() {
        jpaUserRepository.deleteById(tempUserEntity.getId());
    }

    @Nested
    @DisplayName("장바구니 저장")
    class SaveTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testSaveCart() {
            // Given
            CartEntity cartEntity = TestCartEntityFactory.createTestCartEntity(tempUserEntity);

            // When
            CartEntity saveCartEntity = cartRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            CartEntity foundCartEntity = cartRepository.findById(saveCartEntity.getId()).orElse(null);

            // That
            assertThat(foundCartEntity).isNotNull();

            // 지연 로딩인가 ?
            assertThat(foundCartEntity.getUser()).isInstanceOf(HibernateProxy.class);

            // 사용자 매핑 검증
            assertThat(foundCartEntity.getUser().getId()).isEqualTo(tempUserEntity.getId());

            // 하위 항목 검증
            assertThat(foundCartEntity.getCartItems()).isEmpty();
        }

        @Test
        @DisplayName("하위 항목 포함")
        void testSaveCartWithItems() {
            // Given
            List<CartItemEntity> cartItemEntities = TestCartItemEntityFactory.createTestCartItemEntities(3);

            CartEntity cartEntity = TestCartEntityFactory.createTestCartEntity(tempUserEntity);
            cartEntity.changeCartItems(cartItemEntities);

            // When
            CartEntity saveCartEntity = cartRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            CartEntity foundCartEntity = cartRepository.findByUserId(tempUserEntity.getId()).orElse(null);

            assertThat(foundCartEntity).isNotNull();

            // 지연 로딩인가 ?
            assertThat(foundCartEntity.getUser()).isInstanceOf(HibernateProxy.class);

            // 사용자 매핑 검증
            assertThat(foundCartEntity.getUser().getId()).isEqualTo(cartEntity.getUser().getId());

            // 하위 항목 검증
            assertThat(foundCartEntity.getCartItems().size()).isEqualTo(cartItemEntities.size());
            foundCartEntity.getCartItems().forEach(cartItem -> {
                assertThat(cartItem.getId()).isNotNull();
                assertThat(cartItem.getCart().getId()).isEqualTo(saveCartEntity.getId());
            });

        }
    }

    @Nested
    @DisplayName("장바구니 조회")
    class FindTests {

        @Test
        @DisplayName("장바구니 고유키 null 전달")
        void testBadRequestFindById() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartRepository.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("사용자 아이디 null 전달")
        void testBadRequestFindByUserId() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartRepository.findByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("장바구니 존재 확인 null 전달")
        void testBadRequestExistsById() {
            // Given

            // When, Then
            assertThatThrownBy(() -> cartRepository.existsByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("데이터 없음")
        void testNotFoundFindByUserId() {
            // Given

            // When
            Optional<CartEntity> findCart = cartRepository.findByUserId(-999L);

            // Then
            assertThat(findCart).isEmpty();
        }

        @Test
        @DisplayName("조회 성공")
        void testFindByUserId() {

            // Given
            CartEntity cartEntity = TestCartEntityFactory.createTestCartEntity(tempUserEntity);
            cartRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            Optional<CartEntity> findCartEntity = cartRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCartEntity).isPresent();

        }

    }

    @Nested
    @DisplayName("장바구니 삭제")
    class DeleteTests {

        @Test
        @DisplayName("하위 항목 없음")
        void testDeleteById() {

            // Given
            CartEntity cartEntity = TestCartEntityFactory.createTestCartEntity(tempUserEntity);
            CartEntity saveCartEntity = cartRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartRepository.deleteById(saveCartEntity.getId());

            Optional<CartEntity> findCart = cartRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCart).isEmpty();

        }

        @Test
        @DisplayName("하위 항목 포함")
        void testDeleteByIdWithCartItem() {

            // Given
            List<CartItemEntity> cartItemEntities = TestCartItemEntityFactory.createTestCartItemEntities(3);
            CartEntity cartEntity = TestCartEntityFactory.createTestCartEntity(tempUserEntity);
            cartEntity.changeCartItems(cartItemEntities);

            CartEntity saveCartEntity = cartRepository.save(cartEntity);

            // 영속성 컨테이너 초기화
            entityManager.clear();

            // When
            cartRepository.deleteById(saveCartEntity.getId());

            // Then
            Optional<CartEntity> findCart = cartRepository.findByUserId(tempUserEntity.getId());

            assertThat(findCart).isEmpty();

        }

    }
}