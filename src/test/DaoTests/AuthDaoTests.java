package DaoTests;

import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dataAccess.AuthDAO;
import server.dataAccess.DatabaseInterface;
import server.models.AuthToken;

public class AuthDaoTests {
    static Database database = new Database();
    AuthDAO authDAO = new AuthDAO(database);
    String username = "testUsername";

    @BeforeAll
    public static void initialize() {
        database.InitializeDatabase();
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
        database.clearAuthTokens();
        AuthToken authToken = authDAO.addAuthToken(username);
        authDAO.addAuthToken("testUser2");
        authDAO.addAuthToken("testUser3");
        authDAO.removeAuthToken(authToken);
        Assertions.assertFalse(database.hasAuthToken(username), "user has authToken after removal");
        Assertions.assertFalse(authDAO.isAuthorized(authToken), "removed authToken was authorized");
    }
}
