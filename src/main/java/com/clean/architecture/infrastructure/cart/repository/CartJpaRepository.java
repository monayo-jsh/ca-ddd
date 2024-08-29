package com.clean.architecture.infrastructure.cart.repository;

import com.clean.architecture.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
