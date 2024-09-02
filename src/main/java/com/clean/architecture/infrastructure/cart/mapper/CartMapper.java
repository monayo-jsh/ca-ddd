package com.clean.architecture.infrastructure.cart.mapper;

import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = CartItemMapper.class
)
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    // Entity -> Domain Model Mapping
    @Mapping(target = "userId", source = "user.id")
    Cart toDomain(CartEntity source);

    // Domain Model -> Entity Mapping
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    CartEntity toEntity(Cart source);

}
