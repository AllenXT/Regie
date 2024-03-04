package org.courseRegistration.dao;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;

import java.util.List;

public interface SessionDAO {
    // Add a new session record to the database
    void addSession(Session session);

    // Retrieve a session by its unique details (course, session time, and instructor)
    Session getSessionByDetails(Course course, String sessionTime, String instructor);

    Session getSessionByID(String sessionId);

    // Get all sessions for a specific course
    List<Session> getSessionsByCourse(Course course);

    List<Session> getSessionsBySessionTime(String sessionTime);

    // Update an existing session's information
    void updateSessionCourse(String sessionId, Course course);

    void updateSessionSessionTime(String sessionId, String sessionTime);

    void updateSessionInstructor(String sessionId, String instructor);

    void updateSessionLocation(String sessionId, String location);

    void updateSessionMaxStudents(String sessionId, int maxStudents);

    // Delete a session from the database
    void deleteSession(String sessionId);

    // Increment the number of currently registered students for a session
    void incrementCurrentRegistered(String sessionId);

    // Decrement the number of currently registered students for a session
    void decrementCurrentRegistered(String sessionId);

    // Retrieve all sessions taking place at a specific location
    List<Session> getSessionsByLocation(String location);

    // Retrieve all sessions conducted by a specific instructor
    List<Session> getSessionsByInstructor(String instructor);
}
