package com.backend.projectbackend.dto.auth;

import com.backend.projectbackend.util.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class AuthCreateAccountDTO {

    @NotBlank(message = "Email required")
    @Email(message = "Email not valid")
    private String email;

    @NotBlank(message = "Username required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    @Size(max = 20, message = "Username must be shorter than 20 characters")
    private String username;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String passwordConfirm;

    public AuthCreateAccountDTO() {}

    public AuthCreateAccountDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Setters and getters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }
}
