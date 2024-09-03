package com.clean.architecture.application.cart;

import com.clean.architecture.application.cart.mapper.CartUseCaseMapper;
import com.clean.architecture.application.cart.model.CreateCartCommand;
import com.clean.architecture.domain.cart.model.Cart;
import com.clean.architecture.domain.cart.service.CartService;
import com.clean.architecture.domain.user.model.User;
import com.clean.architecture.domain.user.service.UserService;
import com.clean.architecture.presentation.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartUseCase {

    private final UserService userService;
    private final CartService cartService;

    private final CartUseCaseMapper cartUseCaseMapper;

    @Transactional
    public CartResponse createCart(CreateCartCommand command) {
        // 1. 사용자 검증 및 조회
        User user = userService.getUserById(command.userId());

        // 2. 장바구니 검증
        cartService.validateCartExists(user.getId());

        // 3. 장바구니 생성
        Cart cart = Cart.createNew(user.getId());
        Cart saveCart = cartService.createCart(cart);

        // 3. 응답
        return cartUseCaseMapper.toResponse(saveCart);
    }


}
