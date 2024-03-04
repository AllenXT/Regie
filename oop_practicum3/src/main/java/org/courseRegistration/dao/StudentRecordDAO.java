package org.courseRegistration.dao;

import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Student;
import org.courseRegistration.entity.StudentRecord;
import org.courseRegistration.util.Grade;

import java.util.List;
import java.util.Map;

public interface StudentRecordDAO {
    // Add a new student record to the database
    void addStudentRecord(StudentRecord studentRecord);

    // Retrieve a student record by its record ID
    StudentRecord getStudentRecordByRecordId(String recordId);

    StudentRecord getStudentRecordByStudentId(String studentId);

    List<StudentRecord> getStudentRecordByEnrolledYear(String enrolledYear);

    void updateStudentRecordEnrolledYear(String recordId, String enrolledYear);

    // Delete a student record from the database
    void deleteStudentRecord(String recordId);

    // Add or update the grade for a specific course in a student's record
    void insertCourseGrade(String recordId, Course course, Grade grade);
    void updateCourseGrade(String recordId, Course course, Grade grade);

    // Remove a course (and its grade) from a student's record
    void removeCourseFromRecord(String recordId, Course course);

    // Get the grade of a course for a specific student's record
    Grade getGradeOfCourse(String recordId, Course course);

    // Get all courses and grades for a specific student's record
    Map<Course, Grade> getCourseGrades(String recordId);

    // Get a list of all completed courses for a specific student's record
    List<Course> getCompletedCourses(String recordId);

    // Add a completed course to a student's record
    void addCompletedCourse(String recordId, Course course);

    // get the enrolled year of a student
    String getStudentEnrolledYear(Student student);

    // get the student by record ID
    Student getStudentByRecordId(String recordId);
}
