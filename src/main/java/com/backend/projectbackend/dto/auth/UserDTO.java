package com.backend.projectbackend.dto.auth;

import com.backend.projectbackend.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa los datos públicos de un usuario del sistema.")
public class UserDTO {

    @Schema(description = "ID único del usuario (formato hexadecimal de MongoDB).", example = "60d0c4de8e8d8c001f1e4b0a")
    private String id;

    @Schema(description = "Correo electrónico del usuario.", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Nombre de usuario público.", example = "juanperez")
    private String username;

    @Schema(description = "Indica si el usuario tiene rol de administrador.", example = "false")
    private boolean isAdmin;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId().toHexString();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.isAdmin = user.getAdmin();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { this.isAdmin = admin; }
}