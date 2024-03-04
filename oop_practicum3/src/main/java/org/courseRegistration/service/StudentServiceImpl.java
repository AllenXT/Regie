package org.courseRegistration.service;

import org.courseRegistration.dao.*;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;
import org.courseRegistration.entity.Student;
import org.courseRegistration.exceptions.CourseDroppingException;
import org.courseRegistration.exceptions.CourseRegistrationException;
import org.courseRegistration.util.Grade;

import java.util.*;

public class StudentServiceImpl implements StudentService{
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final SessionDAO sessionDAO;
    private final StudentRecordDAO studentRecordDAO;

    public StudentServiceImpl(StudentDAO studentDAO, CourseDAO courseDAO, SessionDAO sessionDAO, StudentRecordDAO studentRecordDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.sessionDAO = sessionDAO;
        this.studentRecordDAO = studentRecordDAO;
    }

    @Override
    public void registerCourse(String studentId, String courseId) throws CourseRegistrationException {
        // check if the course exists
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new CourseRegistrationException("Course does not exist.");
        }

        // check if the student exists
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            throw new CourseRegistrationException("Student does not exist.");
        }

        // check if the student has registered the course
        List<Student> enrolledStudents = studentDAO.getStudentsByCourse(courseId);
        if (enrolledStudents.contains(student)) {
            throw new CourseRegistrationException("Student already enrolled in the course.");
        }

        // check if the session of this course has been full
        boolean isAllSessionsFull = true;
        List<Session> sessions = sessionDAO.getSessionsByCourse(course);
        for (Session session : sessions) {
            if (session.getCurrentRegistered() < session.getMaxStudents()) {
                isAllSessionsFull = false;
                break;
            }
        }
        if(isAllSessionsFull) {
            throw new CourseRegistrationException("All sessions of this course are full.");
        }

        // check if all the prerequisites of this course are meet
        List<Course> prerequisites = courseDAO.getPrerequisites(courseId);
        String recordId = studentRecordDAO.getStudentRecordByStudentId(studentId).getRecordId();
        Set<Course> completedCourses = new HashSet<>(studentRecordDAO.getCompletedCourses(recordId));
        if (!completedCourses.containsAll(prerequisites)) {
            throw new CourseRegistrationException("Student does not meet all prerequisites.");
        }

        // check if the student has enrolled three courses in studentDAOImpl
        studentDAO.addStudentEnrolledCourses(studentId, course);
        String sessionId = sessionDAO.getSessionsByCourse(course).get(0).getSessionId();
        sessionDAO.incrementCurrentRegistered(sessionId);
    }

    @Override
    public void dropCourse(String studentId, String courseId) throws CourseDroppingException {
        // check if the course exists
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new CourseDroppingException("Course does not exist.");
        }

        // check if the student exists
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            throw new CourseDroppingException("Student does not exist.");
        }

        // check if the student has registered the course
        List<Student> enrolledStudents = studentDAO.getStudentsByCourse(courseId);
        if (!enrolledStudents.contains(student)) {
            throw new CourseDroppingException("Student is not enrolled in the course.");
        }

        studentDAO.removeStudentEnrolledCourses(studentId, course);
        String sessionId = sessionDAO.getSessionsByCourse(course).get(0).getSessionId();
        sessionDAO.decrementCurrentRegistered(sessionId);
    }

    @Override
    public List<Session> getCourseSessions(String courseId) {
        Course course = courseDAO.getCourseById(courseId);
        return sessionDAO.getSessionsByCourse(course);
    }

    @Override
    public List<Course> getStudentCourses(String studentId) {
        return studentDAO.getAllStudentEnrolledCourse(studentId);
    }

    @Override
    public Map<Course, Grade> getStudentGrades(String studentId) {
        String recordId = studentRecordDAO.getStudentRecordByStudentId(studentId).getRecordId();
        return studentRecordDAO.getCourseGrades(recordId);
    }

    @Override
    public Course getCourseDetails(String courseId) {
        return courseDAO.getCourseById(courseId);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        List<Course> relatedCourses = new ArrayList<>();
        List<Course> allCourses = courseDAO.getAllCourses();
        for(Course course : allCourses) {
            if(course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                relatedCourses.add(course);
            }
        }
        return relatedCourses;
    }

    @Override
    public List<String> getCourseTimetable(String courseId) {
        List<String> timeTable = new ArrayList<>();
        // check if the course exists
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course does not exist.");
        }
        List<Session> allSessions = sessionDAO.getSessionsByCourse(course);
        for(Session session : allSessions) {
            timeTable.add(session.getSessionTime());
        }
        return timeTable;
    }

    @Override
    public int getTotalCredits(String studentId) {
        int allCredits = 0;
        String recordId = studentRecordDAO.getStudentRecordByStudentId(studentId).getRecordId();
        List<Course> allCompletedCourses = studentRecordDAO.getCompletedCourses(recordId);
        for(Course course : allCompletedCourses) {
            allCredits += course.getCredit();
        }
        return allCredits;
    }

    @Override
    public List<Course> getCoursesWithinPrerequisite(Course prerequisiteCourse) {
        String prerequisiteId = prerequisiteCourse.getCourseId();
        return courseDAO.getCoursesByPrerequisite(prerequisiteId);
    }

    @Override
    public Session searchSpecificSession(Course course, String sessionTime, String instructor) {
        return sessionDAO.getSessionByDetails(course, sessionTime, instructor);
    }

    @Override
    public List<Session> searchSessionsWithSessionTime(String sessionTime) {
        return sessionDAO.getSessionsBySessionTime(sessionTime);
    }

    @Override
    public List<Session> searchSessionsTaughtByProfessor(String instructor) {
        return sessionDAO.getSessionsByInstructor(instructor);
    }

    @Override
    public Grade getGradeOfCourse(String studentId, Course course) {
        String recordId = studentRecordDAO.getStudentRecordByStudentId(studentId).getRecordId();
        return studentRecordDAO.getGradeOfCourse(recordId, course);
    }

    @Override
    public void updateUsername(String studentId, String username) {
        studentDAO.updateStudentUsername(studentId, username);
    }

    @Override
    public void updatePassword(String studentId, String password) {
        studentDAO.updateStudentPassword(studentId, password);
    }

    @Override
    public void updateFirstname(String studentId, String firstname) {
        studentDAO.updateStudentFirstname(studentId, firstname);
    }

    @Override
    public void updateLastname(String studentId, String lastname) {
        studentDAO.updateStudentLastname(studentId, lastname);
    }

    @Override
    public void updateEmail(String studentId, String email) {
        studentDAO.updateStudentEmail(studentId, email);
    }
}
