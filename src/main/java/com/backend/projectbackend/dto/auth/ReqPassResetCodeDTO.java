package com.backend.projectbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ReqPassResetCodeDTO {
    @NotBlank
    @Email
    private String email;

    public ReqPassResetCodeDTO() {}

    public ReqPassResetCodeDTO(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
