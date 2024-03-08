import org.courseRegistration.dao.CourseDAOImpl;
import org.courseRegistration.dao.StudentDAOImpl;
import org.courseRegistration.dao.StudentRecordDAOImpl;
import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;
import org.courseRegistration.entity.StudentRecord;
import org.courseRegistration.util.Grade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRecordDAOTest {
    private final StudentRecordDAOImpl studentRecordDAO = new StudentRecordDAOImpl();
    private final StudentDAOImpl studentDAO = new StudentDAOImpl();
    private final CourseDAOImpl courseDAO = new CourseDAOImpl();
    private final DatabaseManager dbManager = new DatabaseManager();

    @AfterEach
    public void tearDown() {
        // Clean up the database tables to ensure each test starts with a clean state
        dbManager.deleteRecord("StudentRecords", "1=1");
        dbManager.deleteRecord("Courses", "1=1");
        dbManager.deleteRecord("Students", "1=1");
        dbManager.deleteRecord("CourseGrades", "1=1");
        dbManager.deleteRecord("CompletedCourses", "1=1");
    }

    @Test
    public void testAddAndGetStudentRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000001", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        StudentRecord retrievedRecord = studentRecordDAO.getStudentRecordByRecordId("R00000001");
        assertNotNull(retrievedRecord, "Retrieved record should not be null");
        assertEquals(testRecord.getRecordId(), retrievedRecord.getRecordId(), "Record ID does not match");
    }

    @Test
    public void testGetStudentRecordByStudentId() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000001", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        StudentRecord retrievedRecord = studentRecordDAO.getStudentRecordByStudentId("S00000001");
        assertNotNull(retrievedRecord, "Record for student should not be null");
    }

    @Test
    public void testGetStudentRecordByEnrolledYear() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000002", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        List<StudentRecord> records = studentRecordDAO.getStudentRecordByEnrolledYear("2024");
        assertFalse(records.isEmpty(), "Should retrieve records for the enrolled year");
    }

    @Test
    public void testUpdateStudentRecordEnrolledYear() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000003", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        studentRecordDAO.updateStudentRecordEnrolledYear("R00000003", "2025");
        StudentRecord updatedRecord = studentRecordDAO.getStudentRecordByRecordId("R00000003");
        assertEquals("2025", updatedRecord.getEnrolledYear(), "Enrolled year should be updated");
    }

    @Test
    public void testDeleteStudentRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000004", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        studentRecordDAO.deleteStudentRecord("R00000004");
        assertNull(studentRecordDAO.getStudentRecordByRecordId("R00000004"), "Record should be deleted");
    }

    @Test
    public void testInsertCourseGrade() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000005", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course = new Course("MPCS51001", "Software Engineering", 4, "Software development methodologies");
        courseDAO.addCourse(course);
        studentRecordDAO.insertCourseGrade("R00000005", course, Grade.A);
        Grade grade = studentRecordDAO.getGradeOfCourse("R00000005", course);
        assertEquals(Grade.A, grade, "Grade should be A");
    }

    @Test
    public void testUpdateCourseGrade() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000005", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course = new Course("MPCS51001", "Software Engineering", 4, "Software development methodologies");
        courseDAO.addCourse(course);
        studentRecordDAO.insertCourseGrade("R00000005", course, Grade.A);
        Grade grade = studentRecordDAO.getGradeOfCourse("R00000005", course);
        assertEquals(Grade.A, grade, "Grade should be A");

        // Update the grade
        studentRecordDAO.updateCourseGrade("R00000005", course, Grade.B);
        Grade updatedGrade = studentRecordDAO.getGradeOfCourse("R00000005", course);
        assertEquals(Grade.B, updatedGrade, "Grade should be updated to B");
    }

    @Test
    public void testRemoveCourseFromRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000006", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course = new Course("MPCS51015", "Data Structures", 4, "Introduction to data structures");
        courseDAO.addCourse(course);
        studentRecordDAO.insertCourseGrade("R00000006", course, Grade.A);
        studentRecordDAO.removeCourseFromRecord("R00000006", course);
        Grade grade = studentRecordDAO.getGradeOfCourse("R00000006", course);
        assertNull(grade, "Course should be removed from record");
    }

    @Test
    public void testGetCourseGrades() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000007", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course1 = new Course("MPCS51015", "Data Structures", 4, "Introduction to data structures");
        Course course2 = new Course("MPCS51011", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        studentRecordDAO.insertCourseGrade("R00000007", course1, Grade.B);
        studentRecordDAO.insertCourseGrade("R00000007", course2, Grade.C);
        Map<Course, Grade> courseGrades = studentRecordDAO.getCourseGrades("R00000007");
        assertFalse(courseGrades.isEmpty(), "Should retrieve all course grades for the record");
    }

    @Test
    public void testGetCompletedCourses() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000008", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course1 = new Course("MPCS51015", "Data Structures", 4, "Introduction to data structures");
        Course course2 = new Course("MPCS51011", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        studentRecordDAO.addCompletedCourse("R00000008", course1);
        studentRecordDAO.addCompletedCourse("R00000008", course2);
        List<Course> completedCourses = studentRecordDAO.getCompletedCourses("R00000008");
        assertFalse(completedCourses.isEmpty(), "Should retrieve completed courses for the record");
    }

    @Test
    public void testGetNoCompletedCourses() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000009", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        List<Course> completedCourses = studentRecordDAO.getCompletedCourses("R00000009");
        assertTrue(completedCourses.isEmpty(), "Should retrieve no completed courses for the record");
    }

    @Test
    public void testGetStudentEnrolledYear() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000009", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        String year = studentRecordDAO.getStudentEnrolledYear(testStudent);
        assertNotNull(year, "Should retrieve the enrolled year for the student");
        assertEquals("2024", year, "should be the enrolled year in the record");
    }

    @Test
    public void testGetStudentByRecordId() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000010", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Student student = studentRecordDAO.getStudentByRecordId("R00000010");
        assertNotNull(student, "Should retrieve the student for the given record ID");
    }

    @Test
    public void testInvalidRecordIdHandling() {
        StudentRecord record = studentRecordDAO.getStudentRecordByRecordId("InvalidRecordID");
        assertNull(record, "Should return null for an invalid record ID");
    }

    @Test
    public void testInsertGradeForNonexistentRecord() {
        Course course = new Course("MPCS51015", "Algorithms", 4, "Algorithm design and analysis");
        studentRecordDAO.insertCourseGrade("NonexistentRecord", course, Grade.A);
        System.out.println("Adding course grade to non-existent record should be failed!");
    }

    @Test
    public void testUpdateGradeForCourseNotInRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000011", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course = new Course("MPCS51390", "Artificial Intelligence", 4, "Introduction to AI");

        studentRecordDAO.updateCourseGrade("R00000011", course, Grade.B);
        System.out.println("Update grade for course not in record should be failed!");
    }

    @Test
    public void testRemoveNonexistentCourseFromRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000012", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        Course course = new Course("MPCS51550", "Machine Learning", 4, "ML basics");
        // Assume record R00000012 exists but does not have course MPCS51550
        studentRecordDAO.removeCourseFromRecord("R00000012", course);
        System.out.println("Remove non-existent course from record should be failed!");
    }

    @Test
    public void testRetrieveEnrolledYearForNonexistentStudent() {
        Student nonexistentStudent = new Student("S99999999", "Ghost", "Student", "ghost@example.com", "GhostStudent", "Boo123", false);
        String year = studentRecordDAO.getStudentEnrolledYear(nonexistentStudent);
        assertNull(year, "Should not retrieve an enrolled year for a nonexistent student");
    }

    @Test
    public void testAddDuplicateCompletedCourse() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000013", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);

        // Assume record R00000013 exists and course MPCS51550 has been added as completed
        Course course = new Course("MPCS51550", "Machine Learning", 4, "ML basics");
        studentRecordDAO.addCompletedCourse("R00000013", course);
        studentRecordDAO.addCompletedCourse("R00000013", course);
        System.out.println("Adding duplicate completed courses should be failed!");
    }

    @Test
    public void testGetGradesForEmptyRecord() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000014", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        // Assume record R00000014 exists but has no courses
        Map<Course, Grade> courseGrades = studentRecordDAO.getCourseGrades("R00000014");
        assertTrue(courseGrades.isEmpty(), "Should return an empty map for a record with no courses");
    }

    @Test
    public void testDeleteRecordWithDependentData() {
        Student testStudent = new Student("S00000001", "John", "Doe", "testStudent@example.com", "Student001", "Password123", true);
        studentDAO.addStudent(testStudent);
        StudentRecord testRecord = new StudentRecord("R00000015", testStudent, "2024");
        studentRecordDAO.addStudentRecord(testRecord);
        // Assume record R00000015 exists with courses and grades
        studentRecordDAO.deleteStudentRecord("R00000015");
        assertNull(studentRecordDAO.getStudentRecordByRecordId("R00000015"), "Record should be deleted");
    }
}
