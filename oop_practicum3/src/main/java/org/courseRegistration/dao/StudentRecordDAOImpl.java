package org.courseRegistration.dao;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;
import org.courseRegistration.entity.StudentRecord;
import org.courseRegistration.util.Grade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentRecordDAOImpl implements StudentRecordDAO {
    private static final Logger LOGGER = Logger.getLogger(StudentRecordDAOImpl.class.getName());
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void addStudentRecord(StudentRecord studentRecord) {
        String[] columns = {"recordId", "studentId", "enrolledYear"};
        Object[] values = {studentRecord.getRecordId(), studentRecord.getStudent().getStudentId(), studentRecord.getEnrolledYear()};
        boolean result = dbManager.createRecord("StudentRecords", columns, values);
        if(!result) {
            System.out.println("Failed to add student record");
        }
    }

    @Override
    public StudentRecord getStudentRecordByRecordId(String recordId) {
        ResultSet rs = dbManager.readRecords("StudentRecords", "recordId = ?", recordId);
        try {
            if (rs != null && rs.next()) {
                String studentId = rs.getString("studentId");
                String enrolledYear = rs.getString("enrolledYear");
                Student student = getStudentById(studentId);
                if(student == null) {
                    System.out.println("The student is not existed");
                }

                return new StudentRecord(recordId, student, enrolledYear);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying student record by ID failed", e);
        } finally {
            closeResources(rs);
        }
        return null;
    }

    @Override
    public StudentRecord getStudentRecordByStudentId(String studentId) {
        ResultSet rs = dbManager.readRecords("StudentRecords", "studentId = ?", studentId);
        try {
            if (rs != null && rs.next()) {
                String recordId = rs.getString("recordId");
                String enrolledYear = rs.getString("enrolledYear");
                Student student = getStudentById(studentId);
                if(student == null) {
                    System.out.println("The student is not existed");
                }

                return new StudentRecord(recordId, student, enrolledYear);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying student record by student ID failed", e);
        } finally {
            closeResources(rs);
        }
        return null;
    }

    @Override
    public List<StudentRecord> getStudentRecordByEnrolledYear(String enrolledYear) {
        List<StudentRecord> studentRecords = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("StudentRecords", "enrolledYear = ?", enrolledYear);
        if(rs != null) {
            try {
                while (rs.next()) {
                    String recordId = rs.getString("recordId");
                    String studentId = rs.getString("studentId");
                    Student student = getStudentById(studentId);
                    studentRecords.add(new StudentRecord(recordId, student, enrolledYear));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "SQL operation query all student records by enrolled year failed", e);
            } finally {
                closeResources(rs);
            }
            return studentRecords;
        }else {
            System.out.println("There are not students enrolled in this year");
            return null;
        }
    }

    @Override
    public void updateStudentRecordEnrolledYear(String recordId, String enrolledYear) {
        boolean result = dbManager.updateRecord("StudentRecords", "enrolledYear = ?", "recordId = ?", enrolledYear, recordId);
        if (!result) {
            System.out.println("Failed to update student record enrolled year");
        }
    }

    @Override
    public void deleteStudentRecord(String recordId) {
        boolean result = dbManager.deleteRecord("StudentRecords", "recordId = ?", recordId);
        if (!result) {
            System.out.println("Failed to delete student record");
        }
    }

    @Override
    public void insertCourseGrade(String recordId, Course course, Grade grade) {
        String[] columns = {"recordId", "courseId", "grade"};
        Object[] values = {recordId, course.getCourseId(), grade.getDescription()};
        boolean result = dbManager.createRecord("CourseGrades", columns, values);
        if(!result) {
            System.out.println("Failed to insert student course grade");
        }
    }

    @Override
    public void updateCourseGrade(String recordId, Course course, Grade grade) {
        boolean result = dbManager.updateRecord("CourseGrades", "grade = ?", "recordId = ? AND courseId = ?", grade.getDescription(), recordId, course.getCourseId());
        if (!result) {
            System.out.println("Failed to update student course grade");
        }
    }

    @Override
    public void removeCourseFromRecord(String recordId, Course course) {
        boolean result = dbManager.deleteRecord("CourseGrades", "recordId = ? AND courseId = ?", recordId, course.getCourseId());
        if (!result) {
            System.out.println("Failed to remove student course grade from the record");
        }
    }

    @Override
    public Grade getGradeOfCourse(String recordId, Course course) {
        ResultSet rs = dbManager.readRecords("CourseGrades", "recordId = ? AND courseId = ?", recordId, course.getCourseId());
        try {
            if (rs != null && rs.next()) {
                String gradeValue = rs.getString("grade");
                return Grade.valueOf(gradeValue);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying the grade of course failed", e);
        } finally {
            closeResources(rs);
        }
        return null;
    }

    @Override
    public Map<Course, Grade> getCourseGrades(String recordId) {
        Map<Course, Grade> courseGrades = new HashMap<>();
        ResultSet rs = dbManager.readRecords("CourseGrades", "recordId = ?", recordId);
        try {
            while (rs != null && rs.next()) {
                String courseId = rs.getString("courseId");
                Course course = getCourseById(courseId);
                String gradeValue = rs.getString("grade");
                Grade grade = Grade.valueOf(gradeValue);
                courseGrades.put(course, grade);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying the map of courses and grades failed", e);
        } finally {
            closeResources(rs);
        }
        return courseGrades;
    }

    @Override
    public List<Course> getCompletedCourses(String recordId) {
        List<Course> completedCourses = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("CompletedCourses", "recordId = ?", recordId);
        try {
            while (rs != null && rs.next()) {
                String courseId = rs.getString("courseId");
                Course course = getCourseById(courseId);
                completedCourses.add(course);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying all completed courses failed", e);
        } finally {
            closeResources(rs);
        }
        return completedCourses;
    }

    @Override
    public void addCompletedCourse(String recordId, Course course) {
        String[] columns = {"recordId", "courseId"};
        Object[] values = {recordId, course.getCourseId()};
        boolean result = dbManager.createRecord("CompletedCourses", columns, values);
        if(!result) {
            System.out.println("Failed to add student completed course");
        }
    }

    @Override
    public String getStudentEnrolledYear(Student student) {
        // Query to select the enrolled year from StudentRecords table based on studentId
        ResultSet rs = dbManager.readRecords("StudentRecords", "studentId = ?", student.getStudentId());
        try {
            if (rs != null && rs.next()) {
                return rs.getString("enrolledYear");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying student enrolled year failed", e);
        } finally {
            closeResources(rs);
        }
        return null;
    }

    @Override
    public Student getStudentByRecordId(String recordId) {
        ResultSet rs = dbManager.readRecords("StudentRecords", "recordId = ?", recordId);
        try {
            if (rs != null && rs.next()) {
                String studentId = rs.getString("studentId");
                Student student = getStudentById(studentId);
                if(student == null) {
                    System.out.println("The student is not existed");
                }
                return student;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Querying student by record ID failed", e);
        } finally {
            closeResources(rs);
        }
        return null;
    }

    private Course getCourseById(String courseId) {
        ResultSet rs = dbManager.readRecords("Courses", "courseId = ?", courseId);
        if (rs != null) {
            try {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int credit = rs.getInt("credit");
                    String description = rs.getString("description");
                    return new Course(courseId, name, credit, description);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "SQL operation get course by ID failed", e);
            } finally {
                try {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Connection close failed", e);
                }
            }
        }
        return null;
    }

    private Student getStudentById (String studentId) {
        ResultSet rs = dbManager.readRecords("Students", "studentId = ?", studentId);
        if (rs != null) {
            try {
                if (rs.next()) {
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String email = rs.getString("email");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    boolean isActive = rs.getBoolean("isActive");
                    return new Student(studentId, firstName, lastName, email, username, password, isActive);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "SQL operation get student by ID failed", e);
            } finally {
                try {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Connection close failed", e);
                }
            }
        }
        return null;
    }

    private void closeResources(ResultSet rs) {
        try {
            if (rs != null) {
                Connection conn = rs.getStatement().getConnection();
                rs.close();
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Closing resources failed", e);
        }
    }
}
