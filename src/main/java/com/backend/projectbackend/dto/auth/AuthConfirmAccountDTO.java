package com.backend.projectbackend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class AuthConfirmAccountDTO {
    @NotBlank
    private String token;

    public AuthConfirmAccountDTO() {}

    public AuthConfirmAccountDTO(String token) {
        this.token = token;
    }

    //Setters and getters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
