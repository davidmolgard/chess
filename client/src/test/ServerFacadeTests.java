import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import chess.ChessGame;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerFacadeTests {
    private static ServerFacade serverFacade = new ServerFacade();
    private String username = "user";
    private String password = "abc123";
    private AuthToken authToken = null;

    @BeforeEach
    public void initialize() {
        serverFacade.clear();
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email@yes.org");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        authToken = registerResult.getAuthToken();
    }

    @Test
    public void registerPositive() {
        serverFacade.clear();
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email@yes.org");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        authToken = registerResult.getAuthToken();
        Assertions.assertEquals(registerResult.getResponseCode(), 200);
        Assertions.assertEquals(registerResult.getUsername(), username);
        Assertions.assertNotNull(registerResult.getAuthToken());
    }

    @Test
    public void registerNegative() {
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email@yes.org");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        Assertions.assertEquals(registerResult.getResponseCode(), 403);
    }
    @Test
    public void loginPositive(){
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        Assertions.assertEquals(loginResult.getResponseCode(), 200);
        Assertions.assertEquals(loginResult.getUsername(), username);
        Assertions.assertNotNull(loginResult.getAuthToken());
    }

    @Test
    public void loginNegative() {
        LoginRequest loginRequest = new LoginRequest(username, "wrongPassword");
        LoginResult loginResult = serverFacade.login(loginRequest);
        Assertions.assertNotEquals(loginResult.getResponseCode(), 200);
        Assertions.assertNull(loginResult.getAuthToken());
    }

    @Test
    public void logoutPositive() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResult logoutResult = serverFacade.logout(logoutRequest);
        Assertions.assertEquals(logoutResult.getResponseCode(), 200);
    }

    @Test
    public void logoutNegative() {
        LogoutRequest logoutRequest = new LogoutRequest(new AuthToken());
        LogoutResult logoutResult = serverFacade.logout(logoutRequest);
        Assertions.assertEquals(logoutResult.getResponseCode(), 401);
    }

    @Test
    public void createPositive() {
        CreateRequest createRequest = new CreateRequest(authToken, "game1");
        CreateResult createResult = serverFacade.create(createRequest);
        Assertions.assertEquals(createResult.getResponseCode(), 200);
        Assertions.assertInstanceOf(Integer.class, createResult.getGameID());
    }

    @Test
    public void createNegative() {
        CreateRequest createRequest = new CreateRequest(new AuthToken(), "noGame");
        CreateResult createResult = serverFacade.create(createRequest);
        Assertions.assertEquals(createResult.getResponseCode(), 401);
    }

    @Test
    public void joinPositive() {
        CreateRequest createRequest = new CreateRequest(authToken, "noGame");
        CreateResult createResult = serverFacade.create(createRequest);
        int gameID = createResult.getGameID();
        JoinRequest joinRequest = new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID);
        JoinResult joinResult = serverFacade.join(joinRequest);
        Assertions.assertEquals(joinResult.getResponseCode(), 200);
        Assertions.assertNull(joinResult.getMessage());
        joinRequest = new JoinRequest(authToken, ChessGame.TeamColor.BLACK, gameID);
        joinResult = serverFacade.join(joinRequest);
        Assertions.assertEquals(joinResult.getResponseCode(), 200);
        Assertions.assertNull(joinResult.getMessage());
    }

    @Test
    public void joinNegative() {
        CreateRequest createRequest = new CreateRequest(authToken, "noGame");
        CreateResult createResult = serverFacade.create(createRequest);
        int gameID = createResult.getGameID();
        JoinRequest joinRequest = new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID);
        serverFacade.join(joinRequest);
        JoinResult joinResult = serverFacade.join(joinRequest);
        Assertions.assertEquals(joinResult.getResponseCode(), 403);
        joinRequest.setAuthToken(new AuthToken());
        joinResult = serverFacade.join(joinRequest);
        Assertions.assertEquals(joinResult.getResponseCode(), 401);
        joinRequest.setAuthToken(authToken);
        joinRequest.setGameID(0);
        joinResult = serverFacade.join(joinRequest);
        Assertions.assertEquals(joinResult.getResponseCode(), 400);
    }

    @Test
    public void listPositive() {

    }
}
