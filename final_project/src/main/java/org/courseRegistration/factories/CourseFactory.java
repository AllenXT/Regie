package org.courseRegistration.factories;

import org.courseRegistration.entity.Course;

public class CourseFactory implements CourseFactoryI {
    @Override
    public Course createCourse(String courseId, String name, int credit, String description) {
        return new Course(courseId, name, credit, description);
    }
}
