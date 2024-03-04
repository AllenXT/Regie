package org.courseRegistration.factories;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;

public interface SessionFactoryI {
    Session createSession(String sessionId, Course course, String sessionTime, String instructor, String location, int maxStudents, int currentRegistered);
}
