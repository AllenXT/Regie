package org.courseRegistration.service;

import org.courseRegistration.entity.*;
import org.courseRegistration.util.Grade;

import java.util.List;
import java.util.Map;

public interface AdminService {
    // --------------------Administrator Management-----------------------
    // add a new administrator
    void addNewAdmin(Admin admin);

    // get the administrator by ID
    Admin searchAdminByID(String adminId);

    // get the list of all administrators
    List<Admin> getAllAdmins();

    // update admin personal info
    void updateUsername(Admin admin, String username);

    void updatePassword(Admin admin, String password);

    void updateEmail(Admin admin, String email);

    // get all the admins in some role
    List<Admin> searchAdminsByRole(String role);

    // --------------------Course Management-----------------------
    // add a new course
    void addNewCourse(Course course);

    // check the course added
    Course checkCourseById(String courseId);

    // get all current courses
    List<Course> getAllCourses();

    // update course info
    void updateCourseName(Course course, String name);

    void updateCourseCredit(Course course, int credit);

    void updateCourseDescription(Course course, String description);

    // delete a course
    void deleteCourse(Course course);

    // manage course prerequisite
    void addPrerequisite(Course course, Course prerequisite);

    // Remove a prerequisite from a course
    void removePrerequisite(Course course, Course prerequisite);

    // --------------------Session Management-----------------------
    // add a new session
    void addNewSession(Session session);

    // search for a session by ID
    Session searchSessionByID(String sessionId);

    // get all sessions for this course
    List<Session> getSessionsByCourse(Course course);

    // get all sessions in this sessionTime
    List<Session> getSessionsBySessionTime(String sessionTime);

    // update session info
    void updateSessionCourse(Session session, Course course);

    void updateSessionSessionTime(Session session, String sessionTime);

    void updateSessionInstructor(Session session, String instructor);

    void updateSessionLocation(Session session, String location);

    void updateSessionMaxStudents(Session session, int maxStudents);

    // delete a session
    void deleteSession(Session session);

    // --------------------Student Management-----------------------
    // add a new student
    void addNewStudent(Student student);

    // find a student by student ID
    Student findStudentById(String studentId);

    // get all students registered
    List<Student> getAllStudents();

    // delete a student
    void deleteStudent(Student student);

    // activate student status (deactivate before)
    void activateStudent(Student student);

    // deactivate student status
    void deactivateStudent(Student student);

    // check if a student registered a course
    boolean checkIfStudentRegisterCourse(Student student, Course course);

    // --------------------Student Record Management-----------------------
    // add a new student record
    void addStudentRecord(StudentRecord studentRecord);

    // get the student record by student ID
    StudentRecord searchStudentRecord(Student student);

    // get all the students enrolled in a year
    List<Student> searchStudentRecordByEnrolledYear(String enrolledYear);

    // update student enrolled Year
    void updateStudentEnrolledYear(Student student, String enrolledYear);

    // delete a student record
    void deleteStudentRecord(Student student);

    // manage course and grades
    void insertCourseGrades(Student student, Map<Course, Grade> courseGradesAdded);
    void updateCourseGrade(Student student, Course course, Grade grade);
    void removeCourseFromStudentRecord(Student student, Course course);

    // Get all courses and grades of a student
    Map<Course, Grade> searchStudentCourseGrades(Student student);

    // Get a list of all completed courses of a student
    List<Course> getStudentCompletedCourses(Student student);

    // Add a completed course to a student's record
    void addCompletedCourse(Student student, Course course);

    // get the enrolled year of a student
    String findStudentEnrolledYear(Student student);
}
