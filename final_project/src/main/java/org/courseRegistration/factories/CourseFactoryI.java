package org.courseRegistration.factories;

import org.courseRegistration.entity.Course;

public interface CourseFactoryI {
    Course createCourse(String courseId, String name, int credit, String description);
}
