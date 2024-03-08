package org.courseRegistration.factories;

import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;

public class CourseFactory implements CourseFactoryI {
    private final DatabaseManager dbManager;

    public CourseFactory(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Course createCourse(String courseId, String name, int credit, String description) {
        Course course = new Course(courseId, name, credit, description);
        saveCourseToDatabase(course);
        return course;
    }

    private void saveCourseToDatabase(Course course) {
        String table = "Courses";
        String[] columns = {"courseId", "name", "credit", "description"};
        Object[] values = {course.getCourseId(), course.getName(), course.getCredit(), course.getDescription()};

        boolean success = dbManager.createRecord(table, columns, values);
        if (success) {
            System.out.println("Course successfully saved to database.");
        } else {
            System.out.println("Failed to save course to database.");
        }
    }
}
