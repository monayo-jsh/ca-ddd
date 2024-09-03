package com.clean.architecture.presentation.user.dto;

public record UserRequest (

    String username,
    String password,
    String email,
    String phoneNumber

){
}
