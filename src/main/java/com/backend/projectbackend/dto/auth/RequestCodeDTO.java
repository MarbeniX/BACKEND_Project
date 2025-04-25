package com.backend.projectbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RequestCodeDTO {
    @NotBlank
    @Email
    private String email;

    public RequestCodeDTO(){}

    public RequestCodeDTO(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
