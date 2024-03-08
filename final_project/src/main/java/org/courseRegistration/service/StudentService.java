package org.courseRegistration.service;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;
import org.courseRegistration.exceptions.CourseDroppingException;
import org.courseRegistration.exceptions.CourseRegistrationException;
import org.courseRegistration.util.Grade;

import java.util.List;
import java.util.Map;

public interface StudentService {
    // student enroll course
    void registerCourse(String studentId, String courseId) throws CourseRegistrationException;

    // student drop course
    void dropCourse(String studentId, String courseId) throws CourseDroppingException;

    // get all session info of course
    List<Session> getCourseSessions(String courseId);

    // get all courses student currently registered
    List<Course> getStudentCourses(String studentId);

    // get all student course grades
    Map<Course, Grade> getStudentGrades(String studentId);

    // get course details and intro
    Course getCourseDetails(String courseId);

    // search for a course
    List<Course> searchCourses(String keyword);

    // get all session time of the course student want to register
    List<String> getCourseTimetable(String courseId);

    // get all student completed course credits
    int getTotalCredits(String studentId);

    // get courses that have a specific course as a prerequisite
    List<Course> getCoursesWithinPrerequisite(String prerequisiteCourse);

    // search for a specific session
    Session searchSpecificSession(Course course, String sessionTime, String instructor);

    // search for all sessions in this time period
    List<Session> searchSessionsWithSessionTime(String sessionTime);

    // search all courses taught by the professor you like
    List<Session> searchSessionsTaughtByProfessor(String instructor);

    // Get the grade of a course student completed
    Grade getGradeOfCourse(String studentId, Course course);

    void updateUsername(String studentId, String username);

    void updatePassword(String studentId, String password);

    void updateFirstname(String studentId, String firstname);

    void updateLastname(String studentId, String lastname);

    void updateEmail(String studentId, String email);

    // get notification
    // List<Notification> getNotifications(String studentId);
}
