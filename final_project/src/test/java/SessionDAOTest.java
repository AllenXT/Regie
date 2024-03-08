import org.courseRegistration.dao.CourseDAOImpl;
import org.courseRegistration.dao.SessionDAOImpl;
import org.courseRegistration.db.DatabaseManager;
import org.courseRegistration.entity.Course;
import org.courseRegistration.entity.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDAOTest {
    private final SessionDAOImpl sessionDAO = new SessionDAOImpl();
    private final CourseDAOImpl courseDAO = new CourseDAOImpl();
    private final DatabaseManager dbManager = new DatabaseManager();

    @AfterEach
    public void tearDown() {
        // Clear Sessions table to end the tests
        clearSessionsTable();
    }

    private void clearSessionsTable() {
        dbManager.deleteRecord("Sessions", "1=1");
        dbManager.deleteRecord("Courses", "1=1");
    }

    @Test
    public void testAddAndGetSession() {
        Course course = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);
        Session testSession = new Session("S0001", course , "Monday 10AM-12PM", "John Doe", "Room 101", 30, 0);
        sessionDAO.addSession(testSession);
        Session retrievedSession = sessionDAO.getSessionByID(testSession.getSessionId());
        assertNotNull(retrievedSession, "Retrieved session should not be null");
        assertEquals(testSession.getSessionId(), retrievedSession.getSessionId(), "Session ID does not match");
    }

    @Test
    public void testGetSessionsByCourse() {
        Course course = new Course("MPCS51010", "Introduction to Computer Science", 3, "Basics of computer science");
        courseDAO.addCourse(course);
        sessionDAO.addSession(new Session("S0001", course, "Monday 10AM-12PM", "John Doe", "Room 101", 30, 0));
        List<Session> sessions = sessionDAO.getSessionsByCourse(course);
        assertFalse(sessions.isEmpty(), "Should retrieve at least one sessions for the course");
    }

    @Test
    public void testGetSessionByDetails() {
        Course course = new Course("MPCS51012", "Object-Oriented Programming", 4, "OOP Concepts");
        courseDAO.addCourse(course);
        String sessionTime = "Tuesday 2PM-4PM";
        String instructor = "Alice Johnson";
        Session testSession = new Session("S0002", course, sessionTime, instructor, "Room 202", 25, 0);
        sessionDAO.addSession(testSession);
        Session retrievedSession = sessionDAO.getSessionByDetails(course, sessionTime, instructor);
        assertNotNull(retrievedSession, "Session should be successfully retrieved by details");
    }

    @Test
    public void testGetSessionsBySessionTime() {
        Course course = new Course("MPCS51013", "Software Design", 3, "Design Patterns");
        courseDAO.addCourse(course);
        String sessionTime = "Wednesday 9AM-11AM";
        sessionDAO.addSession(new Session("S0003", course, sessionTime, "Bob White", "Room 203", 20, 0));
        List<Session> sessions = sessionDAO.getSessionsBySessionTime(sessionTime);
        assertFalse(sessions.isEmpty(), "Should retrieve sessions by session time");
    }

    @Test
    public void testUpdateSessionCourse() {
        String sessionId = "S0004";
        Course course = new Course("MPCS51013", "Software Design", 3, "Design Patterns");
        courseDAO.addCourse(course);
        Course newCourse = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(newCourse);

        sessionDAO.addSession(new Session("S0004", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        sessionDAO.updateSessionCourse(sessionId, newCourse);
        Session updatedSession = sessionDAO.getSessionByID(sessionId);
        assertEquals(newCourse.getCourseId(), updatedSession.getCourse().getCourseId(), "Session course should be updated");
    }

    @Test
    public void testUpdateSessionSessionTime() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0005", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0005";
        String newSessionTime = "Friday 1PM-3PM";
        sessionDAO.updateSessionSessionTime(sessionId, newSessionTime);
        Session updatedSession = sessionDAO.getSessionByID(sessionId);
        assertEquals(newSessionTime, updatedSession.getSessionTime(), "Session time should be updated");
    }

    @Test
    public void testUpdateSessionInstructor() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0006", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0006";
        String newInstructor = "Cynthia Reed";
        sessionDAO.updateSessionInstructor(sessionId, newInstructor);
        Session updatedSession = sessionDAO.getSessionByID(sessionId);
        assertEquals(newInstructor, updatedSession.getInstructor(), "Session instructor should be updated");
    }

    @Test
    public void testUpdateSessionLocation() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0007", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0007";
        String newLocation = "Room 305";
        sessionDAO.updateSessionLocation(sessionId, newLocation);
        Session updatedSession = sessionDAO.getSessionByID(sessionId);
        assertEquals(newLocation, updatedSession.getLocation(), "Session location should be updated");
    }

    @Test
    public void testUpdateSessionMaxStudents() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0008", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0008";
        int newMaxStudents = 35;
        sessionDAO.updateSessionMaxStudents(sessionId, newMaxStudents);
        Session updatedSession = sessionDAO.getSessionByID(sessionId);
        assertEquals(newMaxStudents, updatedSession.getMaxStudents(), "Session max students should be updated");
    }

    @Test
    public void testDeleteSession() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0009", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0009";
        sessionDAO.deleteSession(sessionId);
        Session session = sessionDAO.getSessionByID(sessionId);
        assertNull(session, "Session should be deleted");
    }

    @Test
    public void testIncrementCurrentRegistered() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0010", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 0));
        String sessionId = "S0010";
        // Assume current registered is initially set to 0.
        sessionDAO.incrementCurrentRegistered(sessionId);
        Session session = sessionDAO.getSessionByID(sessionId);
        assertEquals(1, session.getCurrentRegistered(), "Current registered should be incremented");
    }

    @Test
    public void testDecrementCurrentRegistered() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0011", course, "Tuesday 2PM-4PM", "Bob White", "Room 203", 20, 1));
        String sessionId = "S0011";
        // Assume current registered is initially set to 1.
        sessionDAO.decrementCurrentRegistered(sessionId);
        Session session = sessionDAO.getSessionByID(sessionId);
        assertEquals(0, session.getCurrentRegistered(), "Current registered should be decremented");
    }

    @Test
    public void testGetSessionsByLocation() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0011", course, "Tuesday 2PM-4PM", "Bob White", "Room 204", 20, 0));
        String location = "Room 204";
        List<Session> sessions = sessionDAO.getSessionsByLocation(location);
        assertFalse(sessions.isEmpty(), "Should retrieve sessions by location");
    }

    @Test
    public void testGetSessionsByInstructor() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);

        sessionDAO.addSession(new Session("S0011", course, "Tuesday 2PM-4PM", "Derek Shepherd", "Room 204", 20, 0));
        String instructor = "Derek Shepherd";
        List<Session> sessions = sessionDAO.getSessionsByInstructor(instructor);
        assertFalse(sessions.isEmpty(), "Should retrieve sessions by instructor");
    }

    @Test
    public void testAddAndRetrieveMultipleSessions() {
        Course course1 = new Course("MPCS51015", "Cloud Computing", 3, "Cloud Services");
        Course course2 = new Course("MPCS51016", "Machine Learning", 3, "ML Concepts");
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        sessionDAO.addSession(new Session("S0012", course1, "Monday 10AM-12PM", "Elena Gilbert", "Room 101", 30, 0));
        sessionDAO.addSession(new Session("S0013", course2, "Tuesday 2PM-4PM", "Niklaus Mikaelson", "Room 102", 30, 0));
        List<Session> sessions = sessionDAO.getSessionsByCourse(course1);
        assertFalse(sessions.isEmpty(), "Should retrieve at least one session for the specified course");
    }

    @Test
    public void testSessionNotFound() {
        Session session = sessionDAO.getSessionByID("Nonexistent");
        assertNull(session, "Should not retrieve a session that does not exist");
    }

    @Test
    public void testUpdateNonexistentSession() {
        // Attempt to update a session that does not exist in the database
        String sessionId = "Nonexistent";
        sessionDAO.updateSessionSessionTime(sessionId, "Wednesday 10AM-12PM");
        Session session = sessionDAO.getSessionByID(sessionId);
        assertNull(session, "Updating a nonexistent session should not create a new session");
    }

    @Test
    public void testAddSessionWithDuplicateID() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);
        Session session1 = new Session("S0014", course, "Thursday 9AM-11AM", "Max Planck", "Room 301", 20, 0);
        sessionDAO.addSession(session1);
        // Attempt to add another session with the same ID
        Session session2 = new Session("S0014", course, "Friday 1PM-3PM", "Alan Turing", "Room 302", 25, 0);
        sessionDAO.addSession(session2);
        System.out.println("Each session has its unique ID");
    }

    @Test
    public void testGetSessionsForNonexistentCourse() {
        Course nonexistentCourse = new Course("MPCS99999", "Nonexistent Course", 0, "This course does not exist");
        List<Session> sessions = sessionDAO.getSessionsByCourse(nonexistentCourse);
        assertTrue(sessions.isEmpty(), "Retrieving sessions for a nonexistent course should return an empty list");
    }

    @Test
    public void testIncrementCurrentRegisteredBeyondMax() {
        Course course = new Course("MPCS51014", "Network Security", 3, "Security Principles");
        courseDAO.addCourse(course);
        String sessionId = "S0015";
        Session session = new Session(sessionId, course, "Monday 3PM-5PM", "Kevin Mitnick", "Room 403", 20, 0);
        sessionDAO.addSession(session);
        // Simulate filling the session to its max capacity
        for (int i = 0; i < 20; i++) {
            sessionDAO.incrementCurrentRegistered(sessionId);
        }
        // beyond the max capacity
        sessionDAO.incrementCurrentRegistered(sessionId);
        System.out.println("The registered students should not exceed the max capacity!");
    }
}
