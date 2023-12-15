package server;

import dataAccess.DataAccessException;
import server.dataAccess.*;
import models.AuthToken;
import models.Game;
import models.User;
import RequestResultClasses.clearClasses.ClearRequest;
import RequestResultClasses.clearClasses.ClearResult;
import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.listClasses.ListRequest;
import RequestResultClasses.listClasses.ListResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;

/**
 * Services contains methods for all services offered by the API
 */
public class Services {
    public final int OK = 200;
    public final int BadRequest = 400;
    public final int Unauthorized = 401;
    public final int Forbidden = 403;
    public final int ServerError = 500;
    DatabaseInterface database;
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
            int gameID;
            try {
                gameID = gameDAO.insert(game);
            } catch (DataAccessException e) {
                return new CreateResult(BadRequest, "Error: bad request");
            }
            return new CreateResult(gameID);
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
