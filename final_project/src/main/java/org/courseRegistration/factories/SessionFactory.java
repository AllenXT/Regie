package org.courseRegistration.factories;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;

public class SessionFactory implements SessionFactoryI {
    @Override
    public Session createSession(String sessionId, Course course, String sessionTime, String instructor, String location, int maxStudents, int currentRegistered) {
        return new Session(sessionId, course, sessionTime, instructor, location, maxStudents, currentRegistered);
    }
}
