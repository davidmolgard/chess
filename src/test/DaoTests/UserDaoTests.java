package DaoTests;

import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dataAccess.UserDAO;
import models.User;

public class UserDaoTests {
    private static Database database = new Database();
    private UserDAO userDAO = new UserDAO(database);
    private String username = "testUsername";
    private String email = "email@test.com";
    private String password = "supersecretpassword";
    private User testUser = new User(username, password, email);

    @BeforeAll
    public static void initialize() {
        database.InitializeDatabase();
    }

    @BeforeEach
    public void clear() {
        database.clearUsers();
    }

    @Test
    public void getUserPositive() {
        try {
            database.addUser(testUser, username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        User user = userDAO.getUser(username);
        Assertions.assertNotNull(user, "found user is null");
        Assertions.assertEquals(username, user.getUsername(), "username did not match");
        Assertions.assertEquals(password, user.getPassword(), "password did not match");
        Assertions.assertEquals(email, user.getEmail(), "email did not match");
    }

    @Test
    public void getUserNegative() {
        try {
            database.addUser(testUser, username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(userDAO.getUser("notRealUsername"), "Invalid username did not return null");
    }

    @Test
    public void insertUserPositive() {
        try {
            userDAO.insertUser(testUser, username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        User user = userDAO.getUser(username);
        Assertions.assertNotNull(user, "could not find user");
        Assertions.assertEquals(username, user.getUsername(), "username did not match");
        Assertions.assertEquals(password, user.getPassword(), "password did not match");
        Assertions.assertEquals(email, user.getEmail(), "email did not match");
    }

    @Test
    public void insertUserNegative() {
        Assertions.assertThrows(DataAccessException.class, () -> {
                    userDAO.insertUser(testUser, "incorrectUsername");
                }
                , "invalid insert did not throw DataAccessException");

    }

    @Test
    public void deleteUserPositive() {
        try {
            userDAO.insertUser(testUser, username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            userDAO.deleteUser(username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(userDAO.getUser(username), "user not deleted");
    }

    @Test
    public void deleteUserNegative() {
        try {
            userDAO.insertUser(testUser, username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.deleteUser("badUsername")
                , "deleting nonexistent user did not throw DataAccessException");
    }

    @Test
    public void clearUsers() {
        try {
            userDAO.insertUser(testUser, username);
            userDAO.insertUser(new User("test", "test", "test"), "test");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        userDAO.clearUsers();
        Assertions.assertNull(userDAO.getUser(username), "User still exists after clear");
        Assertions.assertNull(userDAO.getUser("test"), "User still exists after clear");
    }
}
