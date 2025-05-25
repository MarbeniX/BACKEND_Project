package com.backend.projectbackend.dto.auth;

import com.backend.projectbackend.util.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class UpdatePasswordDTO {
    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 8)
    private String passwordConfirm;

    public UpdatePasswordDTO() {}

    public UpdatePasswordDTO(String newPassword, String confirmPassword) {
        this.password = newPassword;
        this.passwordConfirm = confirmPassword;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
