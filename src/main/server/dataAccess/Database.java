package server.dataAccess;

import server.models.AuthToken;
import server.models.Game;
import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface Database {
    HashMap<Integer, Game> games = new HashMap<>();
    ArrayList<User> users = new ArrayList<>();
    ArrayList<AuthToken> authTokens = new ArrayList<>();

    String getGames();
    boolean isAuthorized(AuthToken authToken);
    Game addGame(int GameID);
    void addUser(User user);
    void addAuthToken(AuthToken authToken);
    void removeAuthToken(AuthToken authToken);
    void clearGames();
    void clearUsers();
    void clearAuthTokens();

}
