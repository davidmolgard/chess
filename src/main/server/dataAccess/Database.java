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
    Game[] getGames();
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
     *
     * @param authToken to remove
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
    void clearAll();
    void addGame(Game game, int gameID);
    void addUser(User user, String username);
    void addAuthToken(AuthToken authToken, String username);
    Game getGame(int gameID);
    User getUser(String username);
    void removeGame(int gameID);
    void removeUser(String username);
    void renameGame(int gameID, String newName);
    int getNewGameID();

    boolean hasAuthToken(String username);

    String getUsername(AuthToken authToken);
}
