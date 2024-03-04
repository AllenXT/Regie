package org.courseRegistration.entity;

import org.courseRegistration.util.Grade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRecord {
    private final String recordId;
    private final Student student;
    private final String enrolledYear;

    public StudentRecord(String recordId, Student student, String year) {
        this.recordId = recordId;
        this.student = student;
        this.enrolledYear = year;
    }

    public String getRecordId() {
        return recordId;
    }

    public Student getStudent() {
        return student;
    }

    public String getEnrolledYear() {
        return enrolledYear;
    }
}
