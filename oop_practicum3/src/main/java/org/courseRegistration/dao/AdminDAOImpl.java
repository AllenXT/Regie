package org.courseRegistration.dao;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDAOImpl implements AdminDAO{
    private static final Logger LOGGER = Logger.getLogger(AdminDAOImpl.class.getName());
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void addAdmin(Admin admin) {
        String[] columns = {"adminId", "username", "password", "email", "role", "isActive"};
        Object[] values = {admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getRole(), admin.isActive()};
        boolean result = dbManager.createRecord("Admins", columns, values);
        if(!result) {
            System.out.println("Failed to add administrator");
        }
    }

    @Override
    public Admin getAdminById(String adminId) {
        ResultSet rs = dbManager.readRecords("Admins", "adminId = ?", adminId);
        try {
            if (rs != null && rs.next()) {
                return new Admin(
                        rs.getString("adminId"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query admin failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return null;
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> Administrators = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Admins", "1=1");
        try {
            while (rs != null && rs.next()) {
                Administrators.add(new Admin(
                        rs.getString("adminId"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query all admins failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return Administrators;
    }

    @Override
    public void updateAdminUsername(String adminId, String username) {
        boolean result = dbManager.updateRecord("Admins", "username = ?", "adminId = ?", username, adminId);
        if (!result) {
            System.out.println("Failed to update administrator username");
        }
    }

    @Override
    public void updateAdminPassword(String adminId, String password) {
        boolean result = dbManager.updateRecord("Admins", "password = ?", "adminId = ?", password, adminId);
        if (!result) {
            System.out.println("Failed to update administrator password");
        }
    }

    @Override
    public void updateAdminEmail(String adminId, String email) {
        boolean result = dbManager.updateRecord("Admins", "email = ?", "adminId = ?", email, adminId);
        if (!result) {
            System.out.println("Failed to update administrator email");
        }
    }

    @Override
    public void updateAdminRole(String adminId, String role) {
        boolean result = dbManager.updateRecord("Admins", "role = ?", "adminId = ?", role, adminId);
        if (!result) {
            System.out.println("Failed to update administrator role");
        }
    }

    @Override
    public void deleteAdmin(String adminId) {
        boolean result = dbManager.deleteRecord("Admins", "adminId = ?", adminId);
        if (!result) {
            System.out.println("Failed to delete administrator");
        }
    }

    @Override
    public void activateAdmin(String adminId) {
        // activate administrator status
        boolean result = dbManager.updateRecord("Admins", "isActive = ?", "adminId = ?", true, adminId);
        if (!result) {
            System.out.println("Failed to activate administrator");
        }
    }

    @Override
    public void deactivateAdmin(String adminId) {
        // deactivate administrator status
        boolean result = dbManager.updateRecord("Admins", "isActive = ?", "adminId = ?", false, adminId);
        if (!result) {
            System.out.println("Failed to deactivate administrator");
        }
    }

    @Override
    public List<Admin> getAdminsByRole(String role) {
        List<Admin> administrators = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Admins", "role = ?", role);
        try {
            while (rs != null && rs.next()) {
                administrators.add(new Admin(
                        rs.getString("adminId"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query admins by role failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return administrators;
    }

}
