package com.clean.architecture.infrastructure.cart.mapper;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = CartItemMapper.class
)
public interface CartMapper {

    // Entity -> Domain Model Mapping
    @Mapping(target = "userId", source = "user.id")
    Cart toDomain(CartEntity source);

    // Domain Model -> Entity Mapping
    @Mapping(source = "userId", target = "user.id") // userId를 UserEntity의 id로 매핑
    @Mapping(target = "cartItems", ignore = true)
    CartEntity toEntity(Cart source);

}
