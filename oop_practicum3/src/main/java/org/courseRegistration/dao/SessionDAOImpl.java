package org.courseRegistration.dao;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionDAOImpl implements SessionDAO{
    private static final Logger LOGGER = Logger.getLogger(SessionDAOImpl.class.getName());
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void addSession(Session session) {
        String[] columns = {"sessionId", "courseId", "sessionTime", "instructor", "location", "maxStudents", "currentRegistered"};
        Object[] values = {session.getSessionId(), session.getCourse().getCourseId(), session.getSessionTime(),
                session.getInstructor(), session.getLocation(), session.getMaxStudents(), session.getCurrentRegistered()};
        boolean result = dbManager.createRecord("Sessions", columns, values);
        if(!result) {
            System.out.println("Failed to add session");
        }
    }

    @Override
    public Session getSessionByDetails(Course course, String sessionTime, String instructor) {
        ResultSet rs = dbManager.readRecords("Sessions", "courseId = ? AND sessionTime = ? AND instructor = ?", course.getCourseId(), sessionTime, instructor);
        return extractSessionFromResultSet(rs);
    }

    @Override
    public Session getSessionByID(String sessionId) {
        ResultSet rs = dbManager.readRecords("Sessions", "sessionId = ?", sessionId);
        return extractSessionFromResultSet(rs);
    }

    @Override
    public List<Session> getSessionsByCourse(Course course) {
        List<Session> sessions = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Sessions", "courseId = ?", course.getCourseId());
        Session session;
        while((session = extractSessionFromResultSet(rs)) != null) {
            sessions.add(session);
        }
        return sessions;
    }

    @Override
    public List<Session> getSessionsBySessionTime(String sessionTime) {
        List<Session> sessions = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Sessions", "sessionTime = ?", sessionTime);
        Session session;
        while((session = extractSessionFromResultSet(rs)) != null) {
            sessions.add(session);
        }
        return sessions;
    }

    @Override
    public void updateSessionCourse(String sessionId, Course course) {
        boolean result = dbManager.updateRecord("Sessions", "courseId = ?", "sessionId = ?", course.getCourseId(), sessionId);
        if (!result) {
            System.out.println("Failed to update session course");
        }
    }

    @Override
    public void updateSessionSessionTime(String sessionId, String sessionTime) {
        boolean result = dbManager.updateRecord("Sessions", "sessionTime = ?", "sessionId = ?", sessionTime, sessionId);
        if (!result) {
            System.out.println("Failed to update session time");
        }
    }

    @Override
    public void updateSessionInstructor(String sessionId, String instructor) {
        boolean result = dbManager.updateRecord("Sessions", "instructor = ?", "sessionId = ?", instructor, sessionId);
        if (!result) {
            System.out.println("Failed to update session instructor");
        }
    }

    @Override
    public void updateSessionLocation(String sessionId, String location) {
        boolean result = dbManager.updateRecord("Sessions", "location = ?", "sessionId = ?", location, sessionId);
        if (!result) {
            System.out.println("Failed to update session location");
        }
    }

    @Override
    public void updateSessionMaxStudents(String sessionId, int maxStudents) {
        boolean result = dbManager.updateRecord("Sessions", "maxStudents = ?", "sessionId = ?", maxStudents, sessionId);
        if (!result) {
            System.out.println("Failed to update session student maximum");
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        boolean result = dbManager.deleteRecord("Sessions", "sessionId = ?", sessionId);
        if (!result) {
            System.out.println("Failed to delete session");
        }
    }

    @Override
    public void incrementCurrentRegistered(String sessionId) {
        // This operation should ideally be atomic to avoid race conditions
        // String sql = "UPDATE Sessions SET currentRegistered = currentRegistered + 1 WHERE sessionId = ?";
        boolean result = dbManager.updateRecord("Sessions", "currentRegistered = currentRegistered + 1", "sessionId = ? AND currentRegistered < maxStudents", sessionId);
        if (!result) {
            System.out.println("Failed to increase session current registered student number");
        }
    }

    @Override
    public void decrementCurrentRegistered(String sessionId) {
        // This operation should ideally be atomic to avoid race conditions
        boolean result = dbManager.updateRecord("Sessions", "currentRegistered = currentRegistered - 1", "sessionId = ? AND currentRegistered > 0", sessionId);
        if (!result) {
            System.out.println("Failed to decrease session current registered student number");
        }
    }

    @Override
    public List<Session> getSessionsByLocation(String location) {
        List<Session> sessions = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Sessions", "location = ?", location);
        Session session;
        while ((session = extractSessionFromResultSet(rs)) != null) {
            sessions.add(session);
        }
        return sessions;
    }

    @Override
    public List<Session> getSessionsByInstructor(String instructor) {
        List<Session> sessions = new ArrayList<>();
        ResultSet rs = dbManager.readRecords("Sessions", "instructor = ?", instructor);
        Session session;
        while ((session = extractSessionFromResultSet(rs)) != null) {
            sessions.add(session);
        }
        return sessions;
    }

    private Session extractSessionFromResultSet(ResultSet rs) {
        try {
            if (rs != null && rs.next()) {
                String sessionId = rs.getString("sessionId");
                String courseId = rs.getString("courseId");
                String sessionTime = rs.getString("sessionTime");
                String instructor = rs.getString("instructor");
                String location = rs.getString("location");
                int maxStudents = rs.getInt("maxStudents");
                int currentRegistered = rs.getInt("currentRegistered");
                // Fetch the full course object by courseId
                Course course = getCourseById(courseId);
                if(course == null) {
                    System.out.println("The corresponding course does not exist");
                    return null;
                }
                return new Session(sessionId, course, sessionTime, instructor, location, maxStudents, currentRegistered);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation get sessions failed", e);
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
