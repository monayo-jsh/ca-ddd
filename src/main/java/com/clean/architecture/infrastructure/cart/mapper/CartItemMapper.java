package com.clean.architecture.infrastructure.cart.mapper;

import com.clean.architecture.domain.cart.model.CartItem;
import com.clean.architecture.infrastructure.cart.persistence.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE // 매핑 대상이 없는 경우 무시하고 매핑
)
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    // Entity -> Domain Model Mapping
    @Mapping(target = "cartId", source = "cart.id")
    CartItem toDomain(CartItemEntity source);

    // Domain Model -> Entity Mapping
    @Mapping(target = "cart", ignore = true) // cart는 매핑하지 않음
    CartItemEntity toEntity(CartItem source);

}
