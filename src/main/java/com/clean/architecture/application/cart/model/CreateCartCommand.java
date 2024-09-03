package com.clean.architecture.application.cart.model;

public record CreateCartCommand (

    Long userId,
    String name

) {

}
