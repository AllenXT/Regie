package org.courseRegistration.factories;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;

public class SessionFactory implements SessionFactoryI {
    private final DatabaseManager dbManager;
    public SessionFactory(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Session createSession(String sessionId, Course course, String sessionTime, String instructor, String location, int maxStudents, int currentRegistered) {
        Session session = new Session(sessionId, course, sessionTime, instructor, location, maxStudents, currentRegistered);
        saveSessionToDatabase(session);
        return session;
    }

    private void saveSessionToDatabase(Session session) {
        String table = "Sessions";
        String[] columns = {"sessionId", "courseId", "sessionTime", "instructor", "location", "maxStudents", "currentRegistered"};
        Object[] values = {session.getSessionId(), session.getCourse().getCourseId(), session.getSessionTime(), session.getInstructor(), session.getLocation(), session.getMaxStudents(), session.getCurrentRegistered()};

        boolean success = dbManager.createRecord(table, columns, values);
        if (success) {
            System.out.println("Session successfully saved to database.");
        } else {
            System.out.println("Failed to save session to database.");
        }
    }
}
