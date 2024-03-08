import org.courseRegistration.dao.CourseDAOImpl;
import org.courseRegistration.dao.StudentDAOImpl;
import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {
    private final StudentDAOImpl studentDAO = new StudentDAOImpl();
    private final CourseDAOImpl courseDAO = new CourseDAOImpl();
    private final DatabaseManager dbManager = new DatabaseManager();

    @AfterEach
    public void tearDown() {
        // clear Students table to end the tests
        clearStudentsTable();
    }

    private void clearStudentsTable() {
        dbManager.deleteRecord("Students", "1=1");
        dbManager.deleteRecord("Courses", "1=1");
        dbManager.deleteRecord("EnrolledCourses", "1=1");
    }

    @Test
    public void testAddAndGetStudent() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        Student retrievedStudent = studentDAO.getStudentById("S00000001");
        assertNotNull(retrievedStudent, "Retrieved student should not be null");
        assertEquals(testStudent.getStudentId(), retrievedStudent.getStudentId(), "Student ID does not match");
    }

    @Test
    public void testGetAllStudents() {
        studentDAO.addStudent(new Student("S00000001", "John", "Doe", "testStudent1@example.com", "Student001", "Password123", true));
        studentDAO.addStudent(new Student("S00000002", "Jane", "Doe", "testStudent2@example.com", "Student002", "Password456", true));
        List<Student> students = studentDAO.getAllStudents();
        assertTrue(students.size() >= 2, "Should retrieve at least two students");
    }

    @Test
    public void testUpdateStudentDetails() {
        Student student = new Student("S00000001", "John", "Doe", "testStudent1@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(student);

        // Update different aspects of the student's details
        studentDAO.updateStudentUsername("S00000001", "StudentNewUsername");
        studentDAO.updateStudentEmail("S00000001", "newStudent1@example.com");
        studentDAO.updateStudentFirstname("S00000001", "NewJohn");
        studentDAO.updateStudentLastname("S00000001", "NewDoe");
        studentDAO.updateStudentPassword("S00000001", "NewPassword123");

        Student updatedStudent = studentDAO.getStudentById("S00000001");
        assertEquals("StudentNewUsername", updatedStudent.getUsername(), "Username should be updated");
        assertEquals("newStudent1@example.com", updatedStudent.getEmail(), "Email should be updated");
        assertEquals("NewJohn", updatedStudent.getFirstName(), "First name should be updated");
        assertEquals("NewDoe", updatedStudent.getLastName(), "Last name should be updated");
        assertEquals("NewPassword123", updatedStudent.getPassword(), "Password should be updated");
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student("S00000001", "John", "Doe", "testStudent1@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(student);
        studentDAO.deleteStudent("S00000001");
        assertNull(studentDAO.getStudentById("S00000001"), "Student should be deleted");
    }

    @Test
    public void testActivateAndDeactivateStudent() {
        Student student = new Student("S00000001", "John", "Doe", "testStudent1@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(student);
        studentDAO.deactivateStudent("S00000001");
        assertFalse(studentDAO.getStudentById("S00000001").isActive(), "Student should be deactivated");

        studentDAO.activateStudent("S00000001");
        assertTrue(studentDAO.getStudentById("S00000001").isActive(), "Student should be activated");
    }

    @Test
    public void testAddStudentEnrolledCourses() {
        Student student = new Student("S00000003", "Alice", "Wonder", "alice@example.com", "AliceWonder", "Password789", true);
        Course course = new Course("MPCS51410", "Object Oriented Programming", 4, "oop");
        courseDAO.addCourse(course);
        studentDAO.addStudent(student);
        studentDAO.addStudentEnrolledCourses("S00000003", course);
        List<Course> enrolledCourses = studentDAO.getAllStudentEnrolledCourse("S00000003");
        assertFalse(enrolledCourses.isEmpty(), "Student should be enrolled in at least one course");
    }

    @Test
    public void testRemoveStudentEnrolledCourses() {
        Student student = new Student("S00000003", "Alice", "Wonder", "alice@example.com", "AliceWonder", "Password789", true);
        Course course = new Course("MPCS51410", "Object Oriented Programming", 4, "oop");
        courseDAO.addCourse(course);
        studentDAO.addStudent(student);
        studentDAO.addStudentEnrolledCourses("S00000003", course);
        studentDAO.removeStudentEnrolledCourses("S00000003", course);
        List<Course> enrolledCourses = studentDAO.getAllStudentEnrolledCourse("S00000003");
        assertTrue(enrolledCourses.isEmpty(), "Student should have no courses enrolled");
    }

    @Test
    public void testGetStudentsByCourse() {
        Student student = new Student("S00000003", "Alice", "Wonder", "alice@example.com", "AliceWonder", "Password789", true);
        Course course = new Course("MPCS51410", "Object Oriented Programming", 4, "oop");
        courseDAO.addCourse(course);
        studentDAO.addStudent(student);
        studentDAO.addStudentEnrolledCourses("S00000003", course);
        List<Student> studentsInCourse = studentDAO.getStudentsByCourse("MPCS51410");
        assertFalse(studentsInCourse.isEmpty(), "Should retrieve students enrolled in a specific course");
    }

    @Test
    public void testUpdateStudentEmail() {
        Student student = new Student("S00000004", "Bob", "Builder", "bob@example.com", "BobBuilder", "BuildIt123", true);
        studentDAO.addStudent(student);
        studentDAO.updateStudentEmail("S00000004", "newBob@example.com");
        Student updatedStudent = studentDAO.getStudentById("S00000004");
        assertEquals("newBob@example.com", updatedStudent.getEmail(), "Email should be updated");
    }

    @Test
    public void testDuplicateStudentAddition() {
        Student student = new Student("S00000005", "Charlie", "Chaplin", "charlie@example.com", "CharlieChaplin", "SilentFilm123", true);
        studentDAO.addStudent(student);
        // Attempt to add the same student again
        studentDAO.addStudent(student);
        System.out.println("Duplicate student addition should be failed!");
    }

    @Test
    public void testUpdateNonexistentStudent() {
        // Attempt to update a student that does not exist
        studentDAO.updateStudentEmail("S99999999", "nonexistent@example.com");
        System.out.println("Updating a non-existent student should be failed!");
    }

    @Test
    public void testDeleteNonexistentStudent() {
        // Attempt to delete a student that does not exist
        studentDAO.deleteStudent("S99999999");
        System.out.println("Deleting a non-existent student should be failed!");
    }

    @Test
    public void testActivateNonexistentStudent() {
        // Attempt to activate a student that does not exist
        studentDAO.activateStudent("S99999999");
        System.out.println("Activating a non-existent student should be failed!");
    }

    @Test
    public void testGetStudents() {
        Student student1 = new Student("S00000005", "Charlie", "Chocolate", "charlie@example.com", "Student005", "Chocolate123", true);
        Student student2 = new Student("S00000006", "Danny", "Devito", "danny@example.com", "Student006", "Danny123", true);
        Course course = new Course("MPCS51410", "Object Oriented Programming", 4, "oop");
        courseDAO.addCourse(course);
        studentDAO.addStudent(student1);
        studentDAO.addStudent(student2);
        studentDAO.addStudentEnrolledCourses("S00000005", course);
        studentDAO.addStudentEnrolledCourses("S00000006", course);
        List<Student> enrolledStudents = studentDAO.getStudentsByCourse("MPCS51410");
        assertEquals(2, enrolledStudents.size(), "Should retrieve exactly two students enrolled in the course");
    }

    @Test
    public void testRetrieveStudentsWithNoCourses() {
        Student student = new Student("S00000011", "Helen", "Hunt", "helen@example.com", "Student011", "Hunt123", true);
        studentDAO.addStudent(student);
        List<Course> enrolledCourses = studentDAO.getAllStudentEnrolledCourse("S00000011");
        assertTrue(enrolledCourses.isEmpty(), "Newly added student should not be enrolled in any courses");
    }

    @Test
    public void testRetrieveStudentsByNonexistentCourse() {
        // Attempt to retrieve students by a course that does not exist
        List<Student> students = studentDAO.getStudentsByCourse("MPCS51410");
        assertTrue(students.isEmpty(), "Retrieving students by a non-existent course should return an empty list");
    }

    @Test
    public void testEnrollStudentInNonexistentCourse() {
        Student student = new Student("S00000017", "Mario", "Bro", "mario@example.com", "Student017", "Mario123", true);
        studentDAO.addStudent(student);
        Course nonexistentCourse = new Course("MPCS00000", "Nonexistent Course", 0, "N/A");
        studentDAO.addStudentEnrolledCourses("S00000017", nonexistentCourse);
        System.out.println("Enrolling a student in a nonexistent course should throw an exception");
    }
}
