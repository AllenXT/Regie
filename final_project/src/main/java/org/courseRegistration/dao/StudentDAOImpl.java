package org.courseRegistration.dao;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAOImpl implements StudentDAO{
    // the maximum num of courses a student can register
    private static final int MAX_ENROLLED_COURSES = 3;
    private static final Logger LOGGER = Logger.getLogger(StudentDAOImpl.class.getName());
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void addStudent(Student student) {
        String[] columns = {"studentId", "username", "password", "firstName", "lastName", "email", "isActive"};
        Object[] values = {student.getStudentId(), student.getUsername(), student.getPassword(), student.getFirstName(), student.getLastName(), student.getEmail(), student.isActive()};
        boolean result = dbManager.createRecord("Students", columns, values);
        if(!result) {
            System.out.println("Failed to add student");
        }
    }

    @Override
    public Student getStudentById(String studentId) {
        ResultSet rs = dbManager.readRecords("Students", "studentId = ?", studentId);
        try {
            if (rs != null && rs.next()) {
                return new Student(
                        rs.getString("studentId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query student by ID failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Students", "1=1");
        try {
            while (rs != null && rs.next()) {
                students.add(new Student(
                        rs.getString("studentId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query all students failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return students;
    }

    @Override
    public void updateStudentUsername(String studentId, String username) {
        boolean result = dbManager.updateRecord("Students", "username = ?", "studentId = ?", username, studentId);
        if (!result) {
            System.out.println("Failed to update student username");
        }
    }

    @Override
    public void updateStudentPassword(String studentId, String password) {
        boolean result = dbManager.updateRecord("Students", "password = ?", "studentId = ?", password, studentId);
        if (!result) {
            System.out.println("Failed to update student password");
        }
    }

    @Override
    public void updateStudentFirstname(String studentId, String firstname) {
        boolean result = dbManager.updateRecord("Students", "firstName = ?", "studentId = ?", firstname, studentId);
        if (!result) {
            System.out.println("Failed to update student firstname");
        }
    }

    @Override
    public void updateStudentLastname(String studentId, String lastname) {
        boolean result = dbManager.updateRecord("Students", "lastName = ?", "studentId = ?", lastname, studentId);
        if (!result) {
            System.out.println("Failed to update student lastname");
        }
    }

    @Override
    public void updateStudentEmail(String studentId, String email) {
        boolean result = dbManager.updateRecord("Students", "email = ?", "studentId = ?", email, studentId);
        if (!result) {
            System.out.println("Failed to update student email");
        }
    }

    @Override
    public void addStudentEnrolledCourses(String studentId, Course course) {
        // check if the student has registered three courses
        // String checkSql = "SELECT COUNT(*) AS courseCount FROM EnrolledCourses WHERE studentId = ?";
        ResultSet rs = dbManager.readRecords("EnrolledCourses", "studentId = ?", studentId);
        int count = 0;
        try {
            while (rs != null && rs.next()) {
                count++;
                if (count >= MAX_ENROLLED_COURSES) {
                    // if the student has more than three courses then break the loop
                    break;
                }
            }
            if (count < MAX_ENROLLED_COURSES) {
                // if registered courses less than MAX_ENROLLED_COURSES, it can add a new course
                String[] columns = {"studentId", "courseId"};
                Object[] values = {studentId, course.getCourseId()};
                boolean result = dbManager.createRecord("EnrolledCourses", columns, values);
                if (!result) {
                    System.out.println("Failed to enroll student in course");
                }
            } else {
                // if reach or more than MAX_ENROLLED_COURSES then output the reminder
                System.out.println("Student cannot be enrolled in more than " + MAX_ENROLLED_COURSES + " courses");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation add student enrolled courses failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
    }

    @Override
    public void removeStudentEnrolledCourses(String studentId, Course course) {
        // first check if the student has registered this course
        // String checkSql = "SELECT COUNT(*) AS courseCount FROM StudentCourses WHERE studentId = ? AND courseId = ?";
        ResultSet rs = dbManager.readRecords("EnrolledCourses", "studentId = ? AND courseId = ?", studentId, course.getCourseId());
        try {
            if (rs != null && rs.next()) {
                // if the student has registered the course, we delete the record in the database
                boolean result = dbManager.deleteRecord("EnrolledCourses", "studentId = ? AND courseId = ?", studentId, course.getCourseId());
                if (!result) {
                    System.out.println("Failed to remove course enrollment for student");
                }
            } else {
                // if the student does not enroll in the course, then output error
                System.out.println("Student has not enrolled in this course, cannot remove.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation remove student enrolled courses failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        boolean result = dbManager.deleteRecord("Students", "studentId = ?", studentId);
        if (!result) {
            System.out.println("Failed to delete student");
        }
    }

    @Override
    public void activateStudent(String studentId) {
        // activate student status
        boolean result = dbManager.updateRecord("Students", "isActive = ?", "studentId = ?", true, studentId);
        if (!result) {
            System.out.println("Failed to activate student");
        }
    }

    @Override
    public void deactivateStudent(String studentId) {
        // deactivate student status
        boolean result = dbManager.updateRecord("Students", "isActive = ?", "studentId = ?", false, studentId);
        if (!result) {
            System.out.println("Failed to deactivate student");
        }
    }

    @Override
    public List<Student> getStudentsByCourse(String courseId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM Students s JOIN EnrolledCourses ec ON s.studentId = ec.studentId WHERE ec.courseId = ?";
        ResultSet rs = dbManager.readCustomRecords(sql, courseId);
        try {
            while (rs != null && rs.next()) {
                students.add(new Student(
                        rs.getString("studentId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isActive")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation get students by course failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return students;
    }

    @Override
    public List<Course> getAllStudentEnrolledCourse(String studentId) {
        List<Course> courses = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("EnrolledCourses", "studentId = ?", studentId);
        try {
            while (rs != null && rs.next()) {
                courses.add(getCourseById(rs.getString("courseId")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation get student enrolled courses failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return courses;
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
}
