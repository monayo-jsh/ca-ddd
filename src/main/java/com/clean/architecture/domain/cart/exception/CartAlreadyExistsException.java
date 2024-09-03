package com.clean.architecture.domain.cart.exception;

public class CartAlreadyExistsException extends RuntimeException {

    public CartAlreadyExistsException(Long userId) {
        super("Cart already exists for user: %s".formatted(userId));
    }

}
