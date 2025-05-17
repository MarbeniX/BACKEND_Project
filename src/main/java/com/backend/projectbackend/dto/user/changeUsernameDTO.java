package com.backend.projectbackend.dto.user;

public class changeUsernameDTO {
    private String username;

    public changeUsernameDTO() {}

    public changeUsernameDTO(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
