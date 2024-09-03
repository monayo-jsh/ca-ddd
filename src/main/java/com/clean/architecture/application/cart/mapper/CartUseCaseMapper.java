package com.clean.architecture.application.cart.mapper;

import com.clean.architecture.application.cart.model.CreateCartCommand;
import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.presentation.cart.dto.CartRequest;
import com.clean.architecture.presentation.cart.dto.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CartUseCaseMapper {

    // to Command
    CreateCartCommand toCreateCommand(CartRequest request);

    // to Response
    CartResponse toResponse(Cart cart);

}
