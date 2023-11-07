package service;

import dataAccess.DataAccessException;
import server.dataAccess.*;
import server.models.AuthToken;
import server.models.Game;
import server.models.User;
import service.clearClasses.ClearRequest;
import service.clearClasses.ClearResult;
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

/**
 * Services contains methods for all services offered by the API
 */
public class Services {
    public final int OK = 200;
    public final int BadRequest = 400;
    public final int Unauthorized = 401;
    public final int Forbidden = 403;
    public final int ServerError = 500;
    DatabaseInterface database = new InternalDatabase();
    public Services(DatabaseInterface database) {
        this.database = database;
    }
    /**
     * clears the database if authorized to do so
     * @param req @see ClearRequest
     * @return @see ClearResult (includes error code if error occurred)
     */
    public ClearResult clear(ClearRequest req) {
        database.clearAll();
        return new ClearResult();
    }
    /**
     * creates game if authorized to do so
     * @param req @see CreateRequest
     * @return @see CreateResult (includes error code if error occurred)
     */
    public CreateResult create(CreateRequest req) {
        AuthDAO authDAO = new AuthDAO(database);
        if (req.getAuthToken() == null) {
            return new CreateResult(BadRequest, "Error: bad request");
        }
        else if (!authDAO.isAuthorized(req.getAuthToken())) {
            return new CreateResult(Unauthorized, "Error: unauthorized");
        }
        else {
            GameDAO gameDAO = new GameDAO(database);
            Game game = new Game();
            game.setGameName(req.getGameName());
            return new CreateResult(gameDAO.insert(game));
        }
    }
    /**
     * joins game if authorized to do so
     * @param req @see JoinRequest
     * @return @see JoinResult (includes error code if error occurred)
     */
    public JoinResult join(JoinRequest req) {
        GameDAO gameDAO = new GameDAO(database);
        AuthDAO authDAO = new AuthDAO(database);
        if (!authDAO.isAuthorized(req.getAuthToken())) {
            return new JoinResult(Unauthorized, "Error: unauthorized");
        }
        else if (req.getAuthToken() == null) {
            return new JoinResult(BadRequest, "Error: bad request");
        }
        else {
            try {
                gameDAO.claimSpot(req.getGameID(), authDAO.getUsername(req.getAuthToken()), req.getColor());
            } catch (DataAccessException ex) {
                return new JoinResult(Forbidden, "Error: already taken");
            }
        }
        return new JoinResult();
    }
    /**
     * Lists all games if authorized to do so
     * @param req @see ListRequest
     * @return @see ListResult (includes error code if error occurred)
     */
    public ListResult list(ListRequest req) {
        AuthDAO authDAO = new AuthDAO(database);
        GameDAO gameDAO = new GameDAO(database);
        if (!authDAO.isAuthorized(req.getAuthToken())) {
            return new ListResult(Unauthorized, "Error: unauthorized");
        }
        else {
            return new ListResult(gameDAO.findAll());
        }
    }
    /**
     * Login user if authorized to do so
     * @param req @see LoginRequest
     * @return @see LoginResult (includes error code if error occurred)
     */
    public LoginResult login(LoginRequest req) {
        UserDAO userDAO = new UserDAO(database);
        AuthDAO authDAO = new AuthDAO(database);
        if (userDAO.getUser(req.getUsername()) == null) {
            return new LoginResult(Unauthorized, "Error: unauthorized");
        }
        else if (!req.getPassword().equals(userDAO.getUser(req.getUsername()).getPassword())) {
            return new LoginResult(Unauthorized, "Error: unauthorized");
        }
        else {
            AuthToken newAuthToken = authDAO.addAuthToken(req.getUsername());
            return new LoginResult(req.getUsername(), newAuthToken);
        }

    }
    /**
     * Logout user if authorized to do so
     * @param req @see LogoutRequest
     * @return @see LogoutResult (includes error code if error occurred)
     */
    public LogoutResult logout(LogoutRequest req) {
        AuthDAO authDAO = new AuthDAO(database);
        if (!authDAO.isAuthorized(req.getAuthToken())) {
            return new LogoutResult(Unauthorized, "Error: unauthorized");
        }
        authDAO.removeAuthToken(req.getAuthToken());
        return new LogoutResult();
    }
    /**
     * registers user if authorized to do so
     * @param req @see RegisterRequest
     * @return @see RegisterResult (includes error code if error occurred)
     */
    public RegisterResult register(RegisterRequest req) {
        UserDAO userDAO = new UserDAO(database);
        AuthDAO authDAO = new AuthDAO(database);
        if (userDAO.getUser(req.getUsername()) != null) {
            return new RegisterResult(Forbidden, "Error: already taken");
        }
        else if (req.getUsername() == null || req.getEmail() == null || req.getPassword() == null) {
            return new RegisterResult(BadRequest, "Error: bad request");
        }
        else {
            AuthToken newAuthToken = authDAO.addAuthToken(req.getUsername());
            try {
                userDAO.insertUser(new User(req.getUsername(), req.getPassword(), req.getEmail()), req.getUsername());
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
            return new RegisterResult(req.getUsername(), newAuthToken);
        }
    }
}
