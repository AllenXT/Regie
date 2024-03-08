package org.courseRegistration;

import org.courseRegistration.entity.*;
import org.courseRegistration.exceptions.CourseDroppingException;
import org.courseRegistration.exceptions.CourseRegistrationException;
import org.courseRegistration.service.AdminService;
import org.courseRegistration.service.AdminServiceImpl;
import org.courseRegistration.service.StudentService;
import org.courseRegistration.service.StudentServiceImpl;
import org.courseRegistration.util.Grade;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentServiceImpl();
        AdminService adminService = new AdminServiceImpl();
        boolean exitSystem = false;

        while (!exitSystem) {
            System.out.println("Welcome to the Course Registration System REGIE!");
            System.out.println("Please select your role: ");
            System.out.println("1. Student");
            System.out.println("2. Admin");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline
            switch (choice) {
                case 1:
                    System.out.print("Please enter your student ID: ");
                    String studentId = scanner.nextLine();
                    // Show student menu
                    showStudentMenu(scanner, studentService, studentId);
                    break;
                case 2:
                    // Show admin menu
                    showAdminMenu(scanner, adminService);
                    break;
                case 3:
                    exitSystem = true;
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void showStudentMenu(Scanner scanner, StudentService studentService, String studentId) {
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. Register for a course");
            System.out.println("2. Drop a course");
            System.out.println("3. Get course sessions");
            System.out.println("4. View your courses");
            System.out.println("5. View course timetable");
            System.out.println("6. View your grades");
            System.out.println("7. Update your profile");
            System.out.println("8. Search courses by keyword");
            System.out.println("9. Search a course");
            System.out.println("10. Get your course credits");
            System.out.println("11. Get courses within prerequisite");
            System.out.println("12. Search sessions by professor");
            System.out.println("13. Log out");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter course ID to register: ");
                    String courseId1 = scanner.nextLine();
                    try {
                        studentService.registerCourse(studentId, courseId1);
                        System.out.println("Registered successfully for course " + courseId1);
                    } catch (CourseRegistrationException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter course ID to drop: ");
                    String courseId2 = scanner.nextLine();
                    try {
                        studentService.dropCourse(studentId, courseId2);
                        System.out.println("Course dropped successfully: " + courseId2);
                    } catch (CourseDroppingException e) {
                        System.out.println("Dropping failed: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter course ID you are looking for: ");
                    String courseId3 = scanner.nextLine();
                    List<Session> sessions1 = studentService.getCourseSessions(courseId3);
                    System.out.println("All sessions of the course " + courseId3 + ": ");
                    sessions1.forEach(session -> System.out.println(session.getSessionId() + "  " + session.getSessionTime() + "  " + session.getInstructor() + "  " + session.getLocation() + "  " + session.getCurrentRegistered() + " / " + session.getMaxStudents()));
                    break;
                case 4:
                    List<Course> courses1 = studentService.getStudentCourses(studentId);
                    System.out.println("Your courses:");
                    courses1.forEach(course -> System.out.println(course.getCourseId() + " - " + course.getName()));
                    break;
                case 5:
                    System.out.print("Enter course ID you are looking for: ");
                    String courseId4 = scanner.nextLine();
                    List<String> timetable = studentService.getCourseTimetable(courseId4);
                    System.out.println("The course timetable:");
                    timetable.forEach(System.out::println);
                    break;
                case 6:
                    Map<Course, Grade> grades = studentService.getStudentGrades(studentId);
                    System.out.println("Your grades:");
                    grades.forEach((course, grade) -> System.out.println(course.getName() + ": " + grade.getDescription()));
                    break;
                case 7:
                    updateStudentProfile(scanner, studentService, studentId);
                    break;
                case 8:
                    System.out.print("Enter some keyword related to the course you're interested: ");
                    String keyword = scanner.nextLine();
                    List<Course> courses2 = studentService.searchCourses(keyword);
                    if (courses2.isEmpty()) {
                        System.out.println("No related course results");
                    }else {
                        System.out.println("All courses: ");
                        courses2.forEach(course -> System.out.println(course.getName() + ": " + course.getDescription() + " Credit: " + course.getCredit()));
                    }
                    break;
                case 9:
                    System.out.print("Enter course ID you are looking for: ");
                    String courseId9 = scanner.nextLine();
                    Course course = studentService.getCourseDetails(courseId9);
                    System.out.println(course.getName() + ": " + course.getDescription() + " Credit: " + course.getCredit());
                    break;
                case 10:
                    int totalCredit = studentService.getTotalCredits(studentId);
                    System.out.println("All course credits you have: " + totalCredit);
                    break;
                case 11:
                    System.out.print("Enter prerequisite course ID: ");
                    String prerequisite = scanner.nextLine();
                    List<Course> courses3 = studentService.getCoursesWithinPrerequisite(prerequisite);
                    if (courses3.isEmpty()) {
                        System.out.println("No related course results");
                    }else {
                        System.out.println("All courses: ");
                        courses3.forEach(c -> System.out.println(c.getName() + ": " + c.getDescription() + " Credit: " + c.getCredit()));
                    }
                    break;
                case 12:
                    System.out.print("Enter professor name: ");
                    String name = scanner.nextLine();
                    List<Session> sessions2 = studentService.searchSessionsTaughtByProfessor(name);
                    if (sessions2.isEmpty()) {
                        System.out.println("The professor is not teaching this semester.");
                    }else {
                        System.out.println("All sessions: ");
                        sessions2.forEach(session -> System.out.println(session.getSessionId() + "  " + session.getSessionTime() + "  " + session.getInstructor() + "  " + session.getLocation() + "  " + session.getCurrentRegistered() + " / " + session.getMaxStudents()));
                    }
                    break;
                case 13:
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void updateStudentProfile(Scanner scanner, StudentService studentService, String studentId) {
        System.out.println("Update Profile:");
        System.out.print("Enter new username (leave blank to skip): ");
        String username = scanner.nextLine();
        if (!username.isEmpty()) {
            studentService.updateUsername(studentId, username);
        }

        System.out.print("Enter new password (leave blank to skip): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            studentService.updatePassword(studentId, password);
        }

        System.out.print("Enter new first name (leave blank to skip): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            studentService.updateFirstname(studentId, firstName);
        }

        System.out.print("Enter new last name (leave blank to skip): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            studentService.updateLastname(studentId, lastName);
        }

        System.out.print("Enter new email (leave blank to skip): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            studentService.updateEmail(studentId, email);
        }

        System.out.println("Profile updated successfully.");
    }

    private static void showAdminMenu(Scanner scanner, AdminService adminService) {
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Manage Administrators");
            System.out.println("2. Manage Courses");
            System.out.println("3. Manage Sessions");
            System.out.println("4. Manage Student Records");
            System.out.println("5. Log out");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageAdministrators(scanner, adminService);
                    break;
                case 2:
                    manageCourses(scanner, adminService);
                    break;
                case 3:
                    manageSessions(scanner, adminService);
                    break;
                case 4:
                    manageStudentRecords(scanner, adminService);
                    break;
                case 5:
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageAdministrators(Scanner scanner, AdminService adminService) {
        System.out.println("\nAdministrator Management:");
        System.out.println("1. Add New Administrator");
        System.out.println("2. Search Administrator by ID");
        System.out.println("3. View All Administrators");
        System.out.println("4. Update Administrator Information");
        System.out.println("5. Search Administrators by Role");
        System.out.println("6. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Add a New Administrator:");
                System.out.print("Enter ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter Role: ");
                String role = scanner.nextLine();
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                Admin newAdmin = new Admin(id, email, role, username, password, true);
                adminService.addNewAdmin(newAdmin);
                System.out.println("Administrator added successfully.");
                break;
            case 2:
                System.out.print("Enter Administrator ID to search: ");
                String adminId = scanner.nextLine();
                Admin admin = adminService.searchAdminByID(adminId);
                if (admin != null) {
                    System.out.println("Administrator Found: " + admin);
                } else {
                    System.out.println("No Administrator found with ID: " + adminId);
                }
                break;
            case 3:
                System.out.println("List of All Administrators:");
                List<Admin> admins = adminService.getAllAdmins();
                admins.forEach(System.out::println);
                break;
            case 4:
                System.out.print("Enter Administrator ID for update: ");
                String updateId = scanner.nextLine();
                Admin updateAdmin = adminService.searchAdminByID(updateId);
                if (updateAdmin != null) {
                    System.out.print("Enter new Username (leave blank to keep current): ");
                    String newUsername = scanner.nextLine();
                    if (!newUsername.isEmpty()) {
                        adminService.updateUsername(updateAdmin, newUsername);
                    }
                    System.out.print("Enter new Password (leave blank to keep current): ");
                    String newPassword = scanner.nextLine();
                    if (!newPassword.isEmpty()) {
                        adminService.updatePassword(updateAdmin, newPassword);
                    }
                    System.out.print("Enter new Email (leave blank to keep current): ");
                    String newEmail = scanner.nextLine();
                    if (!newEmail.isEmpty()) {
                        adminService.updateEmail(updateAdmin, newEmail);
                    }
                    System.out.println("Administrator updated successfully.");
                } else {
                    System.out.println("No Administrator found with ID: " + updateId);
                }
                break;
            case 5:
                System.out.print("Enter Role to search Administrators: ");
                String searchRole = scanner.nextLine();
                List<Admin> roleAdmins = adminService.searchAdminsByRole(searchRole);
                if (!roleAdmins.isEmpty()) {
                    System.out.println("Administrators with role " + searchRole + ":");
                    roleAdmins.forEach(System.out::println);
                } else {
                    System.out.println("No Administrators found with role: " + searchRole);
                }
                break;
            case 6:
                // Simply break out of the switch to go back
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void manageCourses(Scanner scanner, AdminService adminService) {
        System.out.println("\nCourse Management:");
        System.out.println("1. Add New Course");
        System.out.println("2. Check Course by ID");
        System.out.println("3. View All Courses");
        System.out.println("4. Update Course Information");
        System.out.println("5. Delete a Course");
        System.out.println("6. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Add a New Course:");
                System.out.print("Enter Course ID: ");
                String courseId1 = scanner.nextLine();
                System.out.print("Enter Course Name: ");
                String courseName = scanner.nextLine();
                System.out.print("Enter Course Credits: ");
                int courseCredits = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter Course Description: ");
                String courseDescription = scanner.nextLine();
                Course newCourse = new Course(courseId1, courseName, courseCredits, courseDescription);
                adminService.addNewCourse(newCourse);
                System.out.println("Course added successfully.");
                break;
            case 2:
                System.out.print("Enter Course ID to check: ");
                String courseId2 = scanner.nextLine();
                Course course = adminService.checkCourseById(courseId2);
                if (course != null) {
                    System.out.println("Course Found: " + course);
                } else {
                    System.out.println("No Course found with ID: " + courseId2);
                }
                break;
            case 3:
                System.out.println("List of All Courses:");
                List<Course> courses = adminService.getAllCourses();
                courses.forEach(System.out::println);
                break;
            case 4:
                System.out.print("Enter Course ID for update: ");
                String courseId3 = scanner.nextLine();
                Course updateCourse = adminService.checkCourseById(courseId3);
                if (updateCourse != null) {
                    System.out.print("Enter new Course Name (leave blank to keep current): ");
                    String newName = scanner.nextLine();
                    if (!newName.isEmpty()) {
                        adminService.updateCourseName(updateCourse, newName);
                    }
                    System.out.print("Enter new Course Credits (leave blank to keep current): ");
                    String newCreditsString = scanner.nextLine();
                    if (!newCreditsString.isEmpty()) {
                        int newCredits = Integer.parseInt(newCreditsString);
                        adminService.updateCourseCredit(updateCourse, newCredits);
                    }
                    System.out.print("Enter new Course Description (leave blank to keep current): ");
                    String newDescription = scanner.nextLine();
                    if (!newDescription.isEmpty()) {
                        adminService.updateCourseDescription(updateCourse, newDescription);
                    }
                    System.out.println("Course updated successfully.");
                } else {
                    System.out.println("No Course found with ID: " + courseId3);
                }
                break;
            case 5:
                System.out.print("Enter Course ID to delete: ");
                String courseId4 = scanner.nextLine();
                Course deleteCourse = adminService.checkCourseById(courseId4);
                if (deleteCourse != null) {
                    adminService.deleteCourse(deleteCourse);
                    System.out.println("Course deleted successfully.");
                } else {
                    System.out.println("No Course found with ID: " + courseId4);
                }
                break;
            case 6:
                // Simply break out of the switch to go back
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void manageSessions(Scanner scanner, AdminService adminService) {
        System.out.println("\nSession Management:");
        System.out.println("1. Add New Session");
        System.out.println("2. Search Session by ID");
        System.out.println("3. Delete a Session");
        System.out.println("4. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Adding a New Session:");
                System.out.print("Enter Session ID: ");
                String sessionId1 = scanner.nextLine();
                System.out.print("Enter Course ID for this session: ");
                String courseId = scanner.nextLine();
                Course course = adminService.checkCourseById(courseId); // This method might need to be implemented to find Course by ID
                System.out.print("Enter Session Time (e.g., Monday 10-12): ");
                String sessionTime = scanner.nextLine();
                System.out.print("Enter Instructor Name: ");
                String instructor = scanner.nextLine();
                System.out.print("Enter Location: ");
                String location = scanner.nextLine();
                System.out.print("Enter Max Students: ");
                int maxStudents = scanner.nextInt();
                Session newSession = new Session(sessionId1, course, sessionTime, instructor, location, maxStudents, 0);
                adminService.addNewSession(newSession);
                System.out.println("Session added successfully.");
                break;
            case 2:
                System.out.print("Enter Session ID to search: ");
                String sessionId2 = scanner.nextLine();
                Session session = adminService.searchSessionByID(sessionId2);
                if (session != null) {
                    System.out.println("Session Found: " + session);
                } else {
                    System.out.println("No Session found with ID: " + sessionId2);
                }
                break;
            case 3:
                System.out.print("Enter Session ID to delete: ");
                String sessionId3 = scanner.nextLine();
                Session sessionToDelete = adminService.searchSessionByID(sessionId3);
                if (sessionToDelete != null) {
                    adminService.deleteSession(sessionToDelete);
                    System.out.println("Session deleted successfully.");
                } else {
                    System.out.println("No Session found with ID: " + sessionId3 + " to delete.");
                }
                break;
            case 4:
                // Simply break out of the switch to go back
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void manageStudentRecords(Scanner scanner, AdminService adminService) {
        System.out.println("\nStudent Record Management:");
        System.out.println("1. Search Student Record by ID");
        System.out.println("2. View All Students Enrolled in a Year");
        System.out.println("3. Update Student Enrolled Year");
        System.out.println("4. Delete a Student Record");
        System.out.println("5. Manage student course grades");
        System.out.println("6. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Student ID to search their record: ");
                String studentId = scanner.nextLine();
                Student student = adminService.findStudentById(studentId);
                if (student != null) {
                    StudentRecord record = adminService.searchStudentRecord(student);
                    System.out.println("Student Record Found: " + record);
                } else {
                    System.out.println("No Student found with ID: " + studentId);
                }
                break;
            case 2:
                System.out.print("Enter the enrolled year to search students: ");
                String enrolledYear = scanner.nextLine();
                List<Student> students = adminService.searchStudentRecordByEnrolledYear(enrolledYear);
                if (!students.isEmpty()) {
                    System.out.println("Students enrolled in " + enrolledYear + ":");
                    students.forEach(System.out::println);
                } else {
                    System.out.println("No students found enrolled in year: " + enrolledYear);
                }
                break;
            case 3:
                System.out.print("Enter Student ID to update enrolled year: ");
                String studentIdForYearUpdate = scanner.nextLine();
                Student studentToUpdateYear = adminService.findStudentById(studentIdForYearUpdate);
                if (studentToUpdateYear != null) {
                    System.out.print("Enter new enrolled year for student " + studentIdForYearUpdate + ": ");
                    String newEnrolledYear = scanner.nextLine();
                    adminService.updateStudentEnrolledYear(studentToUpdateYear, newEnrolledYear);
                    System.out.println("Student's enrolled year updated successfully.");
                } else {
                    System.out.println("No Student found with ID: " + studentIdForYearUpdate);
                }
                break;
            case 4:
                System.out.print("Enter Student ID to delete their record: ");
                String studentIdForRecordDeletion = scanner.nextLine();
                Student studentToDeleteRecord = adminService.findStudentById(studentIdForRecordDeletion);
                if (studentToDeleteRecord != null) {
                    adminService.deleteStudentRecord(studentToDeleteRecord);
                    System.out.println("Student record deleted successfully.");
                } else {
                    System.out.println("No Student found with ID: " + studentIdForRecordDeletion);
                }
                break;
            case 5:
                System.out.print("Enter Student ID to manage their course grades: ");
                String studentIdToManageGrades = scanner.nextLine();
                Student studentToManageGrades = adminService.findStudentById(studentIdToManageGrades);
                if (studentToManageGrades != null) {
                    // get all student courses and grades
                    Map<Course, Grade> courseGrades = adminService.searchStudentCourseGrades(studentToManageGrades);
                    System.out.println("Student's Course Grades:");
                    courseGrades.forEach((course, grade) -> System.out.println(course.getName() + ": " + grade.getDescription()));

                    // get all student completed courses
                    List<Course> completedCourses = adminService.getStudentCompletedCourses(studentToManageGrades);
                    System.out.println("Student's Completed Courses:");
                    completedCourses.forEach(course -> System.out.println(course.getName()));
                } else {
                    System.out.println("No Student found with ID: " + studentIdToManageGrades);
                }
                break;
            case 6:
                // Simply break out of the switch to go back
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

}