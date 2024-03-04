import org.courseRegistration.dao.AdminDAOImpl;
import org.courseRegistration.dao.CourseDAOImpl;
import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminDAOTest {
    private final AdminDAOImpl adminDAO = new AdminDAOImpl();
    private final DatabaseManager dbManager = new DatabaseManager();

    @AfterEach
    public void tearDown() {
        //  clear Admins table to end the tests
        clearAdminsTable();
    }

    private void clearAdminsTable() {
        dbManager.deleteRecord("Admins", "1=1");
    }

    @Test
    public void testAddAndGetAdmin() {
        Admin testAdmin = new Admin("A00000001", "testAdmin@example.com", "faculty", "Admin001", "MPCSAdmin123", true);
        adminDAO.addAdmin(testAdmin);
        Admin retrievedAdmin = adminDAO.getAdminById("A00000001");
        assertNotNull(retrievedAdmin, "Retrieved admin should not be null");
        assertEquals(testAdmin.getAdminId(), retrievedAdmin.getAdminId(), "Admin ID does not match");
    }

    @Test
    public void testGetAllAdmins() {
        adminDAO.addAdmin(new Admin("A00000001", "testAdmin1@example.com", "faculty", "Admin001", "MPCSAdmin123", true));
        adminDAO.addAdmin(new Admin("A00000002", "testAdmin2@example.com", "faculty", "Admin002", "MPCSAdmin456", true));
        List<Admin> admins = adminDAO.getAllAdmins();
        assertTrue(admins.size() >= 2, "Should retrieve at least two admins");
    }

    @Test
    public void testUpdateAdminUsername() {
        Admin admin = new Admin("A00000001", "testAdmin1@example.com", "faculty", "Admin001", "MPCSAdmin123", true);
        adminDAO.addAdmin(admin);
        adminDAO.updateAdminUsername("A00000001", "Admin002");
        Admin updatedAdmin = adminDAO.getAdminById("A00000001");
        assertEquals("Admin002", updatedAdmin.getUsername(), "Username should be updated");
    }

    @Test
    public void testDeleteAdmin() {
        Admin admin = new Admin("A00000001", "testAdmin1@example.com", "faculty", "Admin001", "MPCSAdmin123", true);
        adminDAO.addAdmin(admin);
        adminDAO.deleteAdmin("A00000001");
        assertNull(adminDAO.getAdminById("A00000001"), "Admin should be deleted");
    }

    @Test
    public void testActivateAndDeactivateAdmin() {
        Admin admin = new Admin("A00000001", "testAdmin1@example.com", "faculty", "Admin001", "MPCSAdmin123", true);
        adminDAO.addAdmin(admin);
        adminDAO.deactivateAdmin("A00000001");
        assertFalse(adminDAO.getAdminById("A00000001").isActive(), "Admin should be deactivated");

        adminDAO.activateAdmin("A00000001");
        assertTrue(adminDAO.getAdminById("A00000001").isActive(), "Admin should be activated");
    }

    @Test
    public void testUpdateAdminPassword() {
        Admin admin = new Admin("A00000003", "testAdmin3@example.com", "faculty", "Admin003", "MPCSAdmin789", true);
        adminDAO.addAdmin(admin);
        adminDAO.updateAdminPassword("A00000003", "NewPassword123");
        Admin updatedAdmin = adminDAO.getAdminById("A00000003");
        assertEquals("NewPassword123", updatedAdmin.getPassword(), "Password should be updated");
    }

    @Test
    public void testUpdateAdminEmail() {
        Admin admin = new Admin("A00000004", "testAdmin4@example.com", "faculty", "Admin004", "MPCSAdmin101112", true);
        adminDAO.addAdmin(admin);
        adminDAO.updateAdminEmail("A00000004", "newAdmin4@example.com");
        Admin updatedAdmin = adminDAO.getAdminById("A00000004");
        assertEquals("newAdmin4@example.com", updatedAdmin.getEmail(), "Email should be updated");
    }

    @Test
    public void testUpdateAdminRole() {
        Admin admin = new Admin("A00000005", "testAdmin5@example.com", "faculty", "Admin005", "MPCSAdmin131415", true);
        adminDAO.addAdmin(admin);
        adminDAO.updateAdminRole("A00000005", "staff");
        Admin updatedAdmin = adminDAO.getAdminById("A00000005");
        assertEquals("staff", updatedAdmin.getRole(), "Role should be updated");
    }

    @Test
    public void testGetAdminsByRole() {
        adminDAO.addAdmin(new Admin("A00000006", "testAdmin6@example.com", "faculty", "Admin006", "MPCSAdmin161718", true));
        adminDAO.addAdmin(new Admin("A00000007", "testAdmin7@example.com", "staff", "Admin007", "MPCSAdmin192021", true));
        List<Admin> facultyAdmins = adminDAO.getAdminsByRole("faculty");
        List<Admin> staffAdmins = adminDAO.getAdminsByRole("staff");
        assertFalse(facultyAdmins.isEmpty(), "Should retrieve at least one faculty admin");
        assertFalse(staffAdmins.isEmpty(), "Should retrieve at least one staff admin");
    }

    @Test
    public void testAddDuplicateAdmin() {
        Admin admin1 = new Admin("A00000008", "uniqueAdmin8@example.com", "faculty", "UniqueAdmin008", "MPCSAdmin222", true);
        adminDAO.addAdmin(admin1);
        // Attempt to add the same admin again
        adminDAO.addAdmin(admin1);
        System.out.println("Duplicate admin addition should be failed!");
    }

    @Test
    public void testUpdateNonExistentAdmin() {
        Admin admin = new Admin("A00000009", "nonExistent@example.com", "faculty", "NonExistentAdmin", "MPCSAdmin333", true);
        // Attempt to update an admin that does not exist in the database
        adminDAO.updateAdminEmail("A00000009", "updatedNonExistent@example.com");
        System.out.println("Updating a non-existent admin should be failed!");
    }

    @Test
    public void testDeleteNonExistentAdmin() {
        // Attempt to delete an admin that does not exist in the database
        adminDAO.deleteAdmin("A00000010");
        System.out.println("Deleting a non-existent admin should be failed!");
    }

    @Test
    public void testRetrieveAdminsByNonExistentRole() {
        // Add an admin with a specific role
        adminDAO.addAdmin(new Admin("A00000011", "roleTestAdmin@example.com", "uniqueRole", "RoleTestAdmin", "MPCSAdmin444", true));

        // Attempt to retrieve admins by a role that does not exist
        List<Admin> admins = adminDAO.getAdminsByRole("nonExistentRole");
        assertTrue(admins.isEmpty(), "Retrieving admins by a non-existent role should return an empty list");
    }

    @Test
    public void testSequentialOperations() {
        Admin admin = new Admin("A00000014", "sequentialOpsAdmin@example.com", "faculty", "SequentialOpsAdmin", "MPCSAdmin777", true);
        adminDAO.addAdmin(admin);
        adminDAO.updateAdminEmail("A00000014", "updatedSequentialOps@example.com");
        adminDAO.activateAdmin("A00000014");
        adminDAO.deactivateAdmin("A00000014");

        Admin updatedAdmin = adminDAO.getAdminById("A00000014");
        assertEquals("updatedSequentialOps@example.com", updatedAdmin.getEmail(), "Admin email should be updated");
        assertFalse(updatedAdmin.isActive(), "Admin should be deactivated after sequential operations");
    }

    @Test
    public void testAddAndRetrieveMultipleAdminsByRole() {
        adminDAO.addAdmin(new Admin("A00000015", "multiAdmin1@example.com", "faculty", "MultiAdmin1", "MPCSAdmin888", true));
        adminDAO.addAdmin(new Admin("A00000016", "multiAdmin2@example.com", "staff", "MultiAdmin2", "MPCSAdmin999", true));
        adminDAO.addAdmin(new Admin("A00000017", "multiAdmin3@example.com", "faculty", "MultiAdmin3", "MPCSAdmin000", true));

        List<Admin> facultyAdmins = adminDAO.getAdminsByRole("faculty");
        List<Admin> staffAdmins = adminDAO.getAdminsByRole("staff");

        assertEquals(2, facultyAdmins.size(), "Should retrieve exactly two faculty admins");
        assertEquals(1, staffAdmins.size(), "Should retrieve exactly one staff admin");
    }
}

