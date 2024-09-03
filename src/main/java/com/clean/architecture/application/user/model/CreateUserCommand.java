package com.clean.architecture.application.user.model;

public record CreateUserCommand (

    String username,
    String password,
    String email,
    String phoneNumber

) {
}
