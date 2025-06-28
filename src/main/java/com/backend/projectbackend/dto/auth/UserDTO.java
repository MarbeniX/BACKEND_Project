package com.backend.projectbackend.dto.auth;

import com.backend.projectbackend.model.User;

public class UserDTO {
    private String id;
    private String email;
    private String username;
    private boolean isAdmin;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId().toHexString();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.isAdmin = user.getAdmin();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { this.isAdmin = admin; }
}
