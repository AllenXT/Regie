package org.courseRegistration.service;

import org.courseRegistration.dao.*;
import org.courseRegistration.entity.*;
import org.courseRegistration.util.Grade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService{
    private final AdminDAO adminDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final SessionDAO sessionDAO;
    private final StudentRecordDAO studentRecordDAO;

    public AdminServiceImpl(AdminDAO adminDAO, StudentDAO studentDAO, CourseDAO courseDAO, SessionDAO sessionDAO, StudentRecordDAO studentRecordDAO) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.sessionDAO = sessionDAO;
        this.studentRecordDAO = studentRecordDAO;
    }

    @Override
    public void addNewAdmin(Admin admin) {
        adminDAO.addAdmin(admin);
    }

    @Override
    public Admin searchAdminByID(String adminId) {
        return adminDAO.getAdminById(adminId);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    @Override
    public void updateUsername(Admin admin, String username) {
        adminDAO.updateAdminUsername(admin.getAdminId(), username);
    }

    @Override
    public void updatePassword(Admin admin, String password) {
        adminDAO.updateAdminPassword(admin.getAdminId(), password);
    }

    @Override
    public void updateEmail(Admin admin, String email) {
        adminDAO.updateAdminEmail(admin.getAdminId(), email);
    }

    @Override
    public List<Admin> searchAdminsByRole(String role) {
        return adminDAO.getAdminsByRole(role);
    }

    @Override
    public void addNewCourse(Course course) {
        courseDAO.addCourse(course);
    }

    @Override
    public Course checkCourseById(String courseId) {
        return courseDAO.getCourseById(courseId);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    @Override
    public void updateCourseName(Course course, String name) {
        courseDAO.updateCourseName(course.getCourseId(), name);
    }

    @Override
    public void updateCourseCredit(Course course, int credit) {
        courseDAO.updateCourseCredit(course.getCourseId(), credit);
    }

    @Override
    public void updateCourseDescription(Course course, String description) {
        courseDAO.updateCourseDescription(course.getCourseId(), description);
    }

    @Override
    public void deleteCourse(Course course) {
        courseDAO.deleteCourse(course.getCourseId());
    }

    @Override
    public void addPrerequisite(Course course, Course prerequisite) {
        courseDAO.addPrerequisite(course.getCourseId(), prerequisite);
    }

    @Override
    public void removePrerequisite(Course course, Course prerequisite) {
        courseDAO.removePrerequisite(course.getCourseId(), prerequisite);
    }

    @Override
    public void addNewSession(Session session) {
        sessionDAO.addSession(session);
    }

    @Override
    public Session searchSessionByID(String sessionId) {
        return sessionDAO.getSessionByID(sessionId);
    }

    @Override
    public List<Session> getSessionsByCourse(Course course) {
        return sessionDAO.getSessionsByCourse(course);
    }

    @Override
    public List<Session> getSessionsBySessionTime(String sessionTime) {
        return sessionDAO.getSessionsBySessionTime(sessionTime);
    }

    @Override
    public void updateSessionCourse(Session session, Course course) {
        sessionDAO.updateSessionCourse(session.getSessionId(), course);
    }

    @Override
    public void updateSessionSessionTime(Session session, String sessionTime) {
        sessionDAO.updateSessionSessionTime(session.getSessionId(), sessionTime);
    }

    @Override
    public void updateSessionInstructor(Session session, String instructor) {
        sessionDAO.updateSessionInstructor(session.getSessionId(), instructor);
    }

    @Override
    public void updateSessionLocation(Session session, String location) {
        sessionDAO.updateSessionLocation(session.getSessionId(), location);
    }

    @Override
    public void updateSessionMaxStudents(Session session, int maxStudents) {
        sessionDAO.updateSessionMaxStudents(session.getSessionId(), maxStudents);
    }

    @Override
    public void deleteSession(Session session) {
        sessionDAO.deleteSession(session.getSessionId());
    }

    @Override
    public void addNewStudent(Student student) {
        studentDAO.addStudent(student);
    }

    @Override
    public Student findStudentById(String studentId) {
        return studentDAO.getStudentById(studentId);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public void deleteStudent(Student student) {
        studentDAO.deleteStudent(student.getStudentId());
    }

    @Override
    public void activateStudent(Student student) {
        studentDAO.activateStudent(student.getStudentId());
    }

    @Override
    public void deactivateStudent(Student student) {
        studentDAO.deactivateStudent(student.getStudentId());
    }

    @Override
    public boolean checkIfStudentRegisterCourse(Student student, Course course) {
        List<Student> enrolledStudents = studentDAO.getStudentsByCourse(course.getCourseId());
        if (enrolledStudents.contains(student)) {
            System.out.println("The student has registered this course");
            return true;
        }
        System.out.println("The student never registered this course");
        return false;
    }

    @Override
    public void addStudentRecord(StudentRecord studentRecord) {
        studentRecordDAO.addStudentRecord(studentRecord);
    }

    @Override
    public StudentRecord searchStudentRecord(Student student) {
        return studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
    }

    @Override
    public List<Student> searchStudentRecordByEnrolledYear(String enrolledYear) {
        List<Student> students = new ArrayList<>();
        List<StudentRecord> records = studentRecordDAO.getStudentRecordByEnrolledYear(enrolledYear);
        for(StudentRecord record : records) {
            students.add(studentRecordDAO.getStudentByRecordId(record.getRecordId()));
        }
        return students;
    }

    @Override
    public void updateStudentEnrolledYear(Student student, String enrolledYear) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        studentRecordDAO.updateStudentRecordEnrolledYear(record.getRecordId(), enrolledYear);
    }

    @Override
    public void deleteStudentRecord(Student student) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        studentRecordDAO.deleteStudentRecord(record.getRecordId());
    }

    @Override
    public void insertCourseGrades(Student student, Map<Course, Grade> courseGradesAdded) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        for(Map.Entry<Course, Grade> entry : courseGradesAdded.entrySet()) {
            studentRecordDAO.insertCourseGrade(record.getRecordId(), entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void updateCourseGrade(Student student, Course course, Grade grade) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        studentRecordDAO.updateCourseGrade(record.getRecordId(), course, grade);
    }

    @Override
    public void removeCourseFromStudentRecord(Student student, Course course) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        studentRecordDAO.removeCourseFromRecord(record.getRecordId(), course);
    }

    @Override
    public Map<Course, Grade> searchStudentCourseGrades(Student student) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        return studentRecordDAO.getCourseGrades(record.getRecordId());
    }

    @Override
    public List<Course> getStudentCompletedCourses(Student student) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        return studentRecordDAO.getCompletedCourses(record.getRecordId());
    }

    @Override
    public void addCompletedCourse(Student student, Course course) {
        StudentRecord record = studentRecordDAO.getStudentRecordByStudentId(student.getStudentId());
        studentRecordDAO.addCompletedCourse(record.getRecordId(), course);
    }

    @Override
    public String findStudentEnrolledYear(Student student) {
        return studentRecordDAO.getStudentEnrolledYear(student);
    }
}
