import org.courseRegistration.dao.CourseDAOImpl;
import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {
    private final CourseDAOImpl courseDAO = new CourseDAOImpl();
    private final DatabaseManager dbManager = new DatabaseManager();

    @AfterEach
    public void tearDown() {
        // Clear Courses and Prerequisites tables to end the tests
        clearCoursesTable();
    }

    private void clearCoursesTable() {
        dbManager.deleteRecord("Courses", "1=1");
        dbManager.deleteRecord("CoursePrerequisites", "1=1");
    }

    @Test
    public void testAddAndGetCourse() {
        Course testCourse = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(testCourse);
        Course retrievedCourse = courseDAO.getCourseById("MPCS51010");
        assertNotNull(retrievedCourse, "Retrieved course should not be null");
        assertEquals(testCourse.getCourseId(), retrievedCourse.getCourseId(), "Course ID does not match");
    }

    @Test
    public void testGetAllCourses() {
        courseDAO.addCourse(new Course("MPCS51011", "Introduction to Computer Science", 3, "Basics of computer science"));
        courseDAO.addCourse(new Course("MPCS51414", "Data Structures", 4, "Introduction to data structures"));
        List<Course> courses = courseDAO.getAllCourses();
        assertTrue(courses.size() >= 2, "Should retrieve at least two courses");
    }

    @Test
    public void testUpdateCourseDetails() {
        Course course = new Course("MPCS51011", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);

        // Update different aspects of the course's details
        courseDAO.updateCourseName("MPCS51011", "Intro to CS");
        courseDAO.updateCourseCredit("MPCS51011", 4);
        courseDAO.updateCourseDescription("MPCS51011", "Fundamental concepts of computer science");

        Course updatedCourse = courseDAO.getCourseById("MPCS51011");
        assertEquals("Intro to CS", updatedCourse.getName(), "Course name should be updated");
        assertEquals(4, updatedCourse.getCredit(), "Course credit should be updated");
        assertEquals("Fundamental concepts of computer science", updatedCourse.getDescription(), "Course description should be updated");
    }

    @Test
    public void testDeleteCourse() {
        Course course = new Course("MPCS51011", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);
        courseDAO.deleteCourse("MPCS51011");
        assertNull(courseDAO.getCourseById("MPCS51011"), "Course should be deleted");
    }

    @Test
    public void testAddAndRemovePrerequisite() {
        Course course = new Course("MPCS51014", "Data Structures", 4, "Introduction to data structures");
        Course prerequisite = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);
        courseDAO.addCourse(prerequisite);

        // Add prerequisite
        courseDAO.addPrerequisite("MPCS51014", prerequisite);
        List<Course> prerequisites = courseDAO.getPrerequisites("MPCS51014");
        assertFalse(prerequisites.isEmpty(), "Course should have at least one prerequisite");

        // Remove prerequisite
        courseDAO.removePrerequisite("MPCS51014", prerequisite);
        prerequisites = courseDAO.getPrerequisites("MPCS51014");
        assertTrue(prerequisites.isEmpty(), "Course should have no prerequisites");
    }

    @Test
    public void testGetCoursesByPrerequisite() {
        Course course1 = new Course("MPCS51014", "Data Structures", 4, "Introduction to data structures");
        Course course2 = new Course("MPCS51015", "Algorithms", 4, "Introduction to algorithms");
        Course prerequisite = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        courseDAO.addCourse(prerequisite);

        courseDAO.addPrerequisite("MPCS51014", prerequisite);
        courseDAO.addPrerequisite("MPCS51015", prerequisite);

        List<Course> courses = courseDAO.getCoursesByPrerequisite("MPCS51010");
        assertTrue(courses.size() >= 2, "Should retrieve courses that have the specified course as a prerequisite");
    }

    @Test
    public void testDuplicateCourseAddition() {
        Course course = new Course("MPCS51410", "Software Engineering", 4, "Software development methodologies");
        courseDAO.addCourse(course);
        courseDAO.addCourse(course);
        System.out.println("Duplicate course addition should be rejected");
    }

    @Test
    public void testUpdateNonexistentCourse() {
        courseDAO.updateCourseName("MPCS99999", "Nonexistent Course");
        System.out.println("Updating a non-existent course should be rejected");
    }

    @Test
    public void testDeleteNonexistentCourse() {
        courseDAO.deleteCourse("MPCS99999");
        System.out.println("Deleting a non-existent course should be rejected");
    }

    @Test
    public void testAddPrerequisiteToNonexistentCourse() {
        Course prerequisite = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addPrerequisite("MPCS99999", prerequisite);
        System.out.println("Adding a prerequisite to a nonexistent course should be rejected");
    }

    @Test
    public void testRemovePrerequisiteFromNonexistentCourse() {
        Course prerequisite = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.removePrerequisite("MPCS99999", prerequisite);
        System.out.println("Removing a prerequisite from a nonexistent course should be rejected");
    }

    @Test
    public void testRetrievePrerequisitesForNonexistentCourse() {
        List<Course> prerequisites = courseDAO.getPrerequisites("MPCS99999");
        assertTrue(prerequisites.isEmpty(), "Retrieving prerequisites for a nonexistent course should return an empty list");
    }

    @Test
    public void testGetCoursesByNonexistentPrerequisite() {
        List<Course> courses = courseDAO.getCoursesByPrerequisite("MPCS99999");
        assertTrue(courses.isEmpty(), "Searching for courses by a nonexistent prerequisite should return an empty list");
    }

    @Test
    public void testCourseWithoutPrerequisites() {
        Course course = new Course("MPCS51210", "Database Systems", 4, "Introduction to databases");
        courseDAO.addCourse(course);
        List<Course> prerequisites = courseDAO.getPrerequisites("MPCS51210");
        assertTrue(prerequisites.isEmpty(), "A course without prerequisites should return an empty list of prerequisites");
    }

    @Test
    public void testRemovePrerequisiteNotAssociatedWithCourse() {
        Course course = new Course("MPCS51310", "Computer Networks", 4, "Fundamentals of networking");
        Course prerequisite = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);
        courseDAO.addCourse(prerequisite);
        courseDAO.removePrerequisite("MPCS51310", prerequisite);
        System.out.println("Removing a prerequisite not associated with the course should be rejected");
    }

    @Test
    public void testAddingSelfAsPrerequisite() {
        Course course = new Course("MPCS51050", "Artificial Intelligence", 4, "Introduction to AI concepts");
        courseDAO.addCourse(course);
        courseDAO.addPrerequisite("MPCS51050", course);
        System.out.println("A course cannot be added as its own prerequisite");
    }

}
