package org.courseRegistration.entity;

import org.courseRegistration.entity.User;

import java.util.ArrayList;

public class Admin implements User {
    private String username;
    private String password;
    private final String adminId;
    private String email;
    private String role;
    private boolean isActive;

    public Admin(String adminId, String email, String role, String username, String password, boolean isActive) {
        this.adminId = adminId;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
