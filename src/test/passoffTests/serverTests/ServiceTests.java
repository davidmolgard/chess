package passoffTests.serverTests;

import org.junit.jupiter.api.*;
import chess.*;
import server.dataAccess.DatabaseInterface;
import server.dataAccess.InternalDatabase;
import server.models.AuthToken;
import service.*;
import service.clearClasses.ClearRequest;
import service.createClasses.CreateRequest;
import service.createClasses.CreateResult;
import service.joinClasses.JoinRequest;
import service.joinClasses.JoinResult;
import service.listClasses.ListRequest;
import service.listClasses.ListResult;
import service.loginClasses.LoginRequest;
import service.loginClasses.LoginResult;
import service.logoutClasses.LogoutRequest;
import service.logoutClasses.LogoutResult;
import service.registerClasses.RegisterRequest;
import service.registerClasses.RegisterResult;


public class ServiceTests {

    DatabaseInterface database = new InternalDatabase();
    Services services = new Services(database);
    AuthToken authToken;
    @Test
    @BeforeEach
    public void registerSuccess() {
        services.clear(new ClearRequest());
        RegisterRequest registerRequest = new RegisterRequest("TestUser", "TestPass", "test@yes.com");
        RegisterResult registerResult = services.register(registerRequest);
        Assertions.assertEquals(registerResult.getResponseCode(), 200, "register result not OK");
        authToken = registerResult.getAuthToken();
    }

    @Test
    public void registerFailure() {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        RegisterResult registerResult = services.register(registerRequest);
        Assertions.assertNotEquals(200, registerResult.getResponseCode(), "null entry returned OK" + registerResult.getMessage());
    }
    @Test
    public void createSuccess() {
        CreateRequest createRequest = new CreateRequest(authToken, "testName");
        CreateResult createResult = services.create(createRequest);
        Assertions.assertEquals(200, createResult.getResponseCode(), "valid request did not return OK");
    }

    @Test
    public void createFailure() {
        CreateRequest createRequest = new CreateRequest(new AuthToken(), "failName");
        CreateResult createResult = services.create(createRequest);
        Assertions.assertNotEquals(200, createResult.getResponseCode(), "Invalid request returned OK");
    }

    @Test
    public void joinSuccess() {
        CreateRequest createRequest = new CreateRequest(authToken, "testName");
        CreateResult createResult = services.create(createRequest);
        JoinRequest joinRequest = new JoinRequest(authToken, ChessGame.TeamColor.WHITE, createResult.getGameID());
        JoinResult joinResult = services.join(joinRequest);
        Assertions.assertEquals(200, joinResult.getResponseCode(), "valid join did not return OK");
    }

    @Test
    public void joinFailure() {
        CreateRequest createRequest = new CreateRequest(authToken, "testName");
        CreateResult createResult = services.create(createRequest);
        JoinRequest joinRequest = new JoinRequest(new AuthToken(), ChessGame.TeamColor.WHITE, createResult.getGameID());
        JoinResult joinResult = services.join(joinRequest);
        Assertions.assertNotEquals(200, joinResult.getResponseCode(), "Invalid Join Request returned OK");
    }

    @Test
    public void listSuccess() {
        CreateRequest createRequest = new CreateRequest(authToken, "testName");
        services.create(createRequest);
        ListResult listResult = services.list(new ListRequest(authToken));
        Assertions.assertEquals(200, listResult.getResponseCode(), "valid list did not return OK");
        Assertions.assertEquals("testName", listResult.getGames()[0].getGameName(), "game name not listed");
    }

    @Test
    public void listFailure() {
        CreateRequest createRequest = new CreateRequest(authToken, "testName");
        services.create(createRequest);
        ListResult listResult = services.list(new ListRequest(new AuthToken()));
        Assertions.assertNotEquals(200, listResult.getResponseCode(), "invalid list returned OK");
    }

    @Test
    public void loginSuccess() {
        LoginRequest loginRequest = new LoginRequest("TestUser", "TestPass");
        LoginResult loginResult = services.login(loginRequest);
        Assertions.assertEquals(200, loginResult.getResponseCode(), "valid login did not return OK");
        Assertions.assertNotNull(loginResult.getAuthToken(), "Auth token returned null");
    }

    @Test
    public void loginFailure() {
        LoginRequest loginRequest = new LoginRequest("TestUser", "wrongPass");
        LoginResult loginResult = services.login(loginRequest);
        Assertions.assertNotEquals(200, loginResult.getResponseCode(), "incorrect password returned OK");
        Assertions.assertNull(loginResult.getAuthToken(), "Incorrect password returned AuthToken");
    }

    @Test
    public void logoutSuccess() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResult logoutResult = services.logout(logoutRequest);
        Assertions.assertEquals(200, logoutResult.getResponseCode(), "valid logout did not return OK");
        Assertions.assertFalse(database.isAuthorized(authToken), "AuthToken not removed authorization");
    }

    @Test
    public void logoutFailure() {
        AuthToken badAuthToken = new AuthToken();
        badAuthToken.setAuthToken("yes");
        LogoutRequest logoutRequest = new LogoutRequest(badAuthToken);
        LogoutResult logoutResult = services.logout(logoutRequest);
        Assertions.assertNotEquals(200, logoutResult.getResponseCode(), "invalid logout returned OK");
        Assertions.assertFalse(database.isAuthorized(badAuthToken), "invalid authToken received authorization");
    }

    @Test
    public void clear() {
        registerSuccess();
        createSuccess();
        services.clear(new ClearRequest());
        Assertions.assertEquals(0, database.getGames().length);
        Assertions.assertFalse(database.isAuthorized(authToken));
    }

}
