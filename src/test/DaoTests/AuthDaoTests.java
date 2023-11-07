package DaoTests;

import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dataAccess.AuthDAO;
import server.models.AuthToken;

public class AuthDaoTests {
    static Database database = new Database();
    AuthDAO authDAO = new AuthDAO(database);
    String username = "testUsername";

    @BeforeAll
    public static void initialize() {
        database.InitializeDatabase();
    }

    @BeforeEach
    public void clear() {
        database.clearAuthTokens();
    }

    @Test
    public void isAuthorizedPositive() {
        AuthToken testAuthToken = new AuthToken();
        testAuthToken.setAuthToken("authorized");
        database.addAuthToken(testAuthToken, username);
        Assertions.assertTrue(authDAO.isAuthorized(testAuthToken), "isAuthorized did not return true with authorized token");
    }

    @Test
    public void isAuthorizedNegative() {
        AuthToken testAuthToken = new AuthToken();
        testAuthToken.setAuthToken("authorized");
        database.addAuthToken(testAuthToken, username);
        testAuthToken.setAuthToken("unauthorized");
        Assertions.assertFalse(authDAO.isAuthorized(testAuthToken), "unauthorized token was authorized");
        testAuthToken.setAuthToken(null);
        Assertions.assertFalse(authDAO.isAuthorized(testAuthToken), "null authToken authorized");
    }
    @Test
    public void AddAuthTokenPositive() {
        AuthToken authToken = authDAO.addAuthToken(username);
        Assertions.assertTrue(authDAO.isAuthorized(authToken), "AuthToken not added");
    }

    @Test
    public void AddAuthTokenNegative() {
        AuthToken authToken = authDAO.addAuthToken(username);
        authToken.setAuthToken("badAuthToken");
        Assertions.assertFalse(authDAO.isAuthorized(authToken));
    }

    @Test
    public void RemoveAuthTokenPositive() {
        AuthToken authToken = authDAO.addAuthToken(username);
        authDAO.addAuthToken("testUser2");
        authDAO.addAuthToken("testUser3");
        authDAO.removeAuthToken(authToken);
        Assertions.assertFalse(database.hasAuthToken(username), "user has authToken after removal");
        Assertions.assertFalse(authDAO.isAuthorized(authToken), "removed authToken was authorized");
    }

    @Test
    public void RemoveAuthTokenNegative() {
        AuthToken authToken1 = authDAO.addAuthToken(username);
        AuthToken authToken2 = authDAO.addAuthToken("testUser2");
        AuthToken authToken3 = authDAO.addAuthToken("testUser3");
        authDAO.removeAuthToken(new AuthToken());
        Assertions.assertTrue(authDAO.isAuthorized(authToken1), "empty AuthToken removed another user's token");
        Assertions.assertTrue(authDAO.isAuthorized(authToken2), "empty AuthToken removed another user's token");
        Assertions.assertTrue(authDAO.isAuthorized(authToken3), "empty AuthToken removed another user's token");
    }

    @Test
    public void getUsernamePositive() {
        AuthToken authToken = authDAO.addAuthToken(username);
        Assertions.assertEquals(username, authDAO.getUsername(authToken), "unable to find username/wrong name found");
    }

    @Test
    public void getUsernameNegative() {
        authDAO.addAuthToken(username);
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken("testString");
        Assertions.assertNull(authDAO.getUsername(authToken), "unauthorized token returned username");
        authToken.setAuthToken(null);
        Assertions.assertNull(authDAO.getUsername(authToken), "null authToken returned username");
        AuthToken authTokenNull = authDAO.addAuthToken(null);
        Assertions.assertNull(authDAO.getUsername(authTokenNull), "null username did not return null");
    }

    @Test
    public void clearAuthTokens() {
        AuthToken authToken1 = authDAO.addAuthToken(username);
        AuthToken authToken2 = authDAO.addAuthToken("testUser2");
        AuthToken authToken3 = authDAO.addAuthToken("testUser3");
        authDAO.clearAuthTokens();
        Assertions.assertFalse(authDAO.isAuthorized(authToken1), "failed to clear all authTokens");
        Assertions.assertFalse(authDAO.isAuthorized(authToken2), "failed to clear all authTokens");
        Assertions.assertFalse(authDAO.isAuthorized(authToken3), "failed to clear all authTokens");
    }
}
