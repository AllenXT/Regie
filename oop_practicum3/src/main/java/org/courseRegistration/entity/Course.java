package org.courseRegistration.entity;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final String courseId;
    private String name;
    private int credit;
    private String description;

    // Constructor
    public Course(String courseId, String name, int credit, String description) {
        this.name = name;
        this.courseId = courseId;
        this.credit = credit;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
