package org.courseRegistration.dao;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDAOImpl implements CourseDAO{
    private static final Logger LOGGER = Logger.getLogger(CourseDAOImpl.class.getName());
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void addCourse(Course course) {
        String[] columns = {"courseId", "name", "credit", "description"};
        Object[] values = {course.getCourseId(), course.getName(), course.getCredit(), course.getDescription()};
        boolean result = dbManager.createRecord("Courses", columns, values);
        if(!result) {
            System.out.println("Failed to add course");
        }
    }

    @Override
    public Course getCourseById(String courseId) {
        ResultSet rs = dbManager.readRecords("Courses", "courseId = ?", courseId);
        try {
            if (rs != null && rs.next()) {
                return new Course(
                        rs.getString("courseId"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation get course by ID failed", e);
        } finally {
            try {
                if (rs != null) {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Courses", "1=1");
        try {
            while (rs != null && rs.next()) {
                courses.add(new Course(
                        rs.getString("courseId"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query all courses failed", e);
        } finally {
            try {
                if (rs != null) {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return courses;
    }

    @Override
    public void updateCourseName(String courseId, String name) {
        boolean result = dbManager.updateRecord("Courses", "name = ?", "courseId = ?", name, courseId);
        if (!result) {
            System.out.println("Failed to update course name");
        }
    }

    @Override
    public void updateCourseCredit(String courseId, int credit) {
        boolean result = dbManager.updateRecord("Courses", "credit = ?", "courseId = ?", credit, courseId);
        if (!result) {
            System.out.println("Failed to update course credit");
        }
    }

    @Override
    public void updateCourseDescription(String courseId, String description) {
        boolean result = dbManager.updateRecord("Courses", "description = ?", "courseId = ?", description, courseId);
        if (!result) {
            System.out.println("Failed to update course description");
        }
    }

    @Override
    public void deleteCourse(String courseId) {
        boolean result = dbManager.deleteRecord("Courses", "courseId = ?", courseId);
        if (!result) {
            System.out.println("Failed to delete course");
        }
    }

    @Override
    public void addPrerequisite(String courseId, Course prerequisite) {
        String[] columns = {"courseId", "prerequisiteId"};
        Object[] values = {courseId, prerequisite.getCourseId()};
        boolean result = dbManager.createRecord("CoursePrerequisites", columns, values);
        if (!result) {
            System.out.println("Failed to add course prerequisite");
        }
    }

    @Override
    public void removePrerequisite(String courseId, Course prerequisite) {
        boolean result = dbManager.deleteRecord("CoursePrerequisites", "courseId = ? AND prerequisiteId = ?", courseId, prerequisite.getCourseId());
        if (!result) {
            System.out.println("Failed to remove course prerequisite");
        }
    }

    @Override
    public List<Course> getPrerequisites(String courseId) {
        List<Course> prerequisites = new ArrayList<>();
        String sql = "SELECT c.courseId, c.name, c.credit, c.description FROM Courses c INNER JOIN CoursePrerequisites cp ON c.courseId = cp.prerequisiteId WHERE cp.courseId = ?";
        ResultSet rs = dbManager.readCustomRecords(sql, courseId);
        try {
            while (rs != null && rs.next()) {
                prerequisites.add(new Course(
                        rs.getString("courseId"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query course prerequisites failed", e);
        } finally {
            try {
                if (rs != null) {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return prerequisites;
    }

    @Override
    public List<Course> getCoursesByPrerequisite(String prerequisiteId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.courseId, c.name, c.credit, c.description FROM Courses c INNER JOIN CoursePrerequisites cp ON c.courseId = cp.courseId WHERE cp.prerequisiteId = ?";
        ResultSet rs = dbManager.readCustomRecords(sql, prerequisiteId);
        try {
            while (rs != null && rs.next()) {
                courses.add(new Course(
                        rs.getString("courseId"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation query courses by prerequisite failed", e);
        } finally {
            try {
                if (rs != null) {
                    Connection conn = rs.getStatement().getConnection();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Connection close failed", e);
            }
        }
        return courses;
    }
}
