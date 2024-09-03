package com.clean.architecture.presentation.cart;

import com.clean.architecture.application.cart.CartUseCase;
import com.clean.architecture.application.cart.mapper.CartUseCaseMapper;
import com.clean.architecture.application.cart.model.CreateCartCommand;
import com.clean.architecture.presentation.cart.dto.CartRequest;
import com.clean.architecture.presentation.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartUseCase cartUseCase;
    private final CartUseCaseMapper cartUseCaseMapper;

    @PostMapping("")
    public ResponseEntity<CartResponse> createCart(@RequestBody CartRequest request) {
        CreateCartCommand command = cartUseCaseMapper.toCreateCommand(request);
        CartResponse cart = cartUseCase.createCart(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(cart);
    }
}
