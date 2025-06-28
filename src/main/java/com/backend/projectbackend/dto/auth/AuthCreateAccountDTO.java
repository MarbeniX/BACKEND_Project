package com.backend.projectbackend.dto.auth;

import com.backend.projectbackend.util.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema; // Importar la anotación
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
@Schema(description = "Datos requeridos para la creación de una nueva cuenta de usuario.")
public class AuthCreateAccountDTO {

    @Schema(description = "Correo electrónico del usuario. Debe ser una dirección de email válida y única.",
            example = "usuario@ejemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email required")
    @Email(message = "Email not valid")
    private String email;

    @Schema(description = "Nombre de usuario único. Debe tener entre 3 y 20 caracteres.",
            example = "nuevo_usuario123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    @Size(max = 20, message = "Username must be shorter than 20 characters")
    private String username;

    @Schema(description = "Contraseña del usuario. Mínimo 8 caracteres.",
            example = "MiClaveSegura123!", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description = "Confirmación de la contraseña. Debe coincidir exactamente con el campo 'password'.",
            example = "MiClaveSegura123!", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
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