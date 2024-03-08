package org.courseRegistration.dao;

import org.courseRegistration.entity.Course;

import java.util.List;

public interface CourseDAO {
    // Add a new course to the database
    void addCourse(Course course);

    // Retrieve a course by its course ID
    Course getCourseById(String courseId);

    // Get a list of all courses
    List<Course> getAllCourses();

    // Update an existing course's information
    void updateCourseName(String courseId, String name);

    void updateCourseCredit(String courseId, int credit);

    void updateCourseDescription(String courseId, String description);

    // Delete a course from the database using its course ID
    void deleteCourse(String courseId);

    // Add a prerequisite to a course
    void addPrerequisite(String courseId, Course prerequisite);

    // Remove a prerequisite from a course
    void removePrerequisite(String courseId, Course prerequisite);

    // Get a list of prerequisites for a given course
    List<Course> getPrerequisites(String courseId);

    // Get courses that have a specific course as a prerequisite
    List<Course> getCoursesByPrerequisite(String prerequisiteId);
}
