package com.app.vaazar.identity.application.dto;

public class RegisterCommand {
    private String email;
    private String password;
    private String role;

    public RegisterCommand(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
