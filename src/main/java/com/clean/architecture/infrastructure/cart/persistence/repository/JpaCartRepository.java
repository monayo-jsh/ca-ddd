package com.clean.architecture.infrastructure.cart.persistence.repository;

import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCartRepository extends JpaRepository<CartEntity, Long> {
}
