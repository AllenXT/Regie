-- Create a new database only if it does not already exist
CREATE DATABASE IF NOT EXISTS CourseRegistration;

-- Select the database to use
USE CourseRegistration;

-- Create table for Students
CREATE TABLE IF NOT EXISTS Students (
    studentId VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    isActive BOOLEAN NOT NULL
);

-- Create table for Admins
CREATE TABLE IF NOT EXISTS Admins (
    adminId VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(255) NOT NULL,
    isActive BOOLEAN NOT NULL
);

-- Create table for Courses
CREATE TABLE IF NOT EXISTS Courses (
    courseId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    credit INT NOT NULL,
    description TEXT
);

-- Create table for Courses student have registered
CREATE TABLE IF NOT EXISTS EnrolledCourses (
    studentId VARCHAR(255),
    courseId VARCHAR(255),
    PRIMARY KEY (studentId, courseId),
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE
);

-- Courses prerequisites (self-referencing many-to-many relationship)
CREATE TABLE IF NOT EXISTS CoursePrerequisites (
    courseId VARCHAR(255) NOT NULL,
    prerequisiteId VARCHAR(255) NOT NULL,
    PRIMARY KEY (courseId, prerequisiteId),
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    FOREIGN KEY (prerequisiteId) REFERENCES Courses(courseId) ON DELETE CASCADE
);

-- Create table for Sessions
CREATE TABLE IF NOT EXISTS Sessions (
    sessionId VARCHAR(255) PRIMARY KEY,
    courseId VARCHAR(255) NOT NULL,
    sessionTime VARCHAR(255) NOT NULL,
    instructor VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    maxStudents INT NOT NULL,
    currentRegistered INT NOT NULL,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE
);

-- Create table for StudentRecords
CREATE TABLE IF NOT EXISTS StudentRecords (
    recordId VARCHAR(255) PRIMARY KEY,
    studentId VARCHAR(255) NOT NULL,
    enrolledYear VARCHAR(255) NOT NULL,
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE
);

-- Create table for CourseGrades within StudentRecords
CREATE TABLE IF NOT EXISTS CourseGrades (
    recordId VARCHAR(255) NOT NULL,
    courseId VARCHAR(255) NOT NULL,
    grade VARCHAR(255) NOT NULL,
    PRIMARY KEY (recordId, courseId),
    FOREIGN KEY (recordId) REFERENCES StudentRecords(recordId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE
);

-- Create table for CompletedCourses within StudentRecords
CREATE TABLE IF NOT EXISTS CompletedCourses (
    recordId VARCHAR(255) NOT NULL,
    courseId VARCHAR(255) NOT NULL,
    PRIMARY KEY (recordId, courseId),
    FOREIGN KEY (recordId) REFERENCES StudentRecords(recordId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE
);

-- Indexes for better query performance
CREATE INDEX idx_student_email ON Students(email);
CREATE INDEX idx_admin_email ON Admins(email);


