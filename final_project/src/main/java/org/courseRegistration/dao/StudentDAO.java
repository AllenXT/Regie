package org.courseRegistration.dao;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;

import java.util.List;

public interface StudentDAO {
    // Create a new student record in the database
    void addStudent(Student student);

    // Retrieve a student by their student ID
    Student getStudentById(String studentId);

    // Retrieve a list of all students
    List<Student> getAllStudents();

    // Update an existing student's information
    void updateStudentUsername(String studentId, String username);

    void updateStudentPassword(String studentId, String password);

    void updateStudentFirstname(String studentId, String firstname);

    void updateStudentLastname(String studentId, String lastname);

    void updateStudentEmail(String studentId, String email);

    void addStudentEnrolledCourses(String studentId, Course course);

    void removeStudentEnrolledCourses(String studentId, Course course);

    // Delete a student record from the database
    void deleteStudent(String studentId);

    // Activate a student's status
    void activateStudent(String studentId);

    // Deactivate a student's status
    void deactivateStudent(String studentId);

    // Retrieve a list of students enrolled in a specific course
    List<Student> getStudentsByCourse(String courseId);

    // get all student currently registered courses
    List<Course> getAllStudentEnrolledCourse(String studentId);
}
