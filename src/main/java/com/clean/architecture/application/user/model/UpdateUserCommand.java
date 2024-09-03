package com.clean.architecture.application.user.model;

public record UpdateUserCommand (

    Long id,
    String username,
    String password,
    String email,
    String phoneNumber

){
}
