package org.courseRegistration.dao;

import org.courseRegistration.entity.Admin;

import java.util.List;

public interface AdminDAO {
    // Adds a new admin record to the database
    void addAdmin(Admin admin);

    // Retrieves an admin by their unique admin ID
    Admin getAdminById(String adminId);

    // Retrieves a list of all admins in the database
    List<Admin> getAllAdmins();

    // Updates an existing admin information in the database
    void updateAdminUsername(String adminId, String username);

    void updateAdminPassword(String adminId, String password);

    void updateAdminEmail(String adminId, String email);

    void updateAdminRole(String adminId, String role);

    // Deletes an admin record from the database using their admin ID
    void deleteAdmin(String adminId);

    // Activates an admin account, enabling them to log in and perform actions
    void activateAdmin(String adminId);

    // Deactivates an admin account, preventing them from logging in and performing actions
    void deactivateAdmin(String adminId);

    // Retrieves a list of admins by their role
    List<Admin> getAdminsByRole(String role);
}
