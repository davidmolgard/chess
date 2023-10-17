package server.dataAccess;

import server.models.AuthToken;
import server.models.Game;
import server.models.User;

import java.util.HashMap;

/**
 * Database Interface
 * Stores Hashmaps of Games, Users, and AuthTokens in Hashmaps
 * Allows the Data to be stored in a Database or in memory according to the implementation
 */
public interface Database {
    HashMap<Integer, Game> games = new HashMap<>();
    HashMap<String, User> users = new HashMap<>();
    HashMap<String, AuthToken> authTokens = new HashMap<>();

    /**
     * @return String representation of the Hashmap storing the games
     */
    String getGames();
    /**
     * @return String representation of the Hashmap storing the users
     */
    String getUsers();
    /**
     * @return String representation of the Hashmap storing the Authorization Tokens
     */
    String getAuthTokens();

    /**
     * @param authToken of user to see if they are authorized
     * @return true if valid authorization token is provided, false if not
     */
    boolean isAuthorized(AuthToken authToken);

    /**
     * @param authToken to be removed from the Hashmap
     */
    void removeAuthToken(AuthToken authToken);

    /**
     * clears Hashmap of games
     */
    void clearGames();

    /**
     * clears hashmap of users
     */
    void clearUsers();

    /**
     * clears hashmap of authorization tokens
     */
    void clearAuthTokens();
    void addGame(Game game);
    void addUser(User user);
    void addAuthToken(AuthToken authToken);
    Game getGame(int GameID);
    User getUser(String username);
    AuthToken getAuthToken(String username);
}
