package org.courseRegistration.entity;

import org.courseRegistration.entity.Course;

public class Session {
    private final String sessionId;
    private Course course;
    private String sessionTime;
    private String instructor;
    private String location;
    private int maxStudents;
    private int currentRegistered;

    // Constructor
    public Session(String sessionId, Course course, String sessionTime, String instructor, String location, int maxStudents, int currentRegistered) {
        this.sessionId = sessionId;
        this.course = course;
        this.sessionTime = sessionTime;
        this.instructor = instructor;
        this.location = location;
        this.maxStudents = maxStudents;
        this.currentRegistered = currentRegistered;
    }

    public String getSessionId() {
        return sessionId;
    }

    // Getters and Setters
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getCurrentRegistered() {
        return currentRegistered;
    }

    public void setCurrentRegistered(int currentRegistered) {
        this.currentRegistered = currentRegistered;
    }
}
