package server.dataAccess;

import server.models.AuthToken;
import server.models.Game;
import server.models.User;

import java.util.HashMap;

public class InternalDatabase implements Database{
    HashMap<Integer, Game> games = new HashMap<>();
    HashMap<AuthToken, String> authTokens = new HashMap<>();
    HashMap<String, User> users = new HashMap<>();
    @Override
    public String getGames() {
        return games.toString();
    }

    @Override
    public String getUsers() {
        return users.toString();
    }

    @Override
    public String getAuthTokens() {
        return authTokens.toString();
    }

    @Override
    public boolean isAuthorized(AuthToken authToken) {
        return authTokens.containsKey(authToken);
    }

    @Override
    public void removeAuthToken(AuthToken authToken) {
        authTokens.remove(authToken);
    }


    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public void clearUsers() {
        users.clear();
    }

    @Override
    public void clearAuthTokens() {
        authTokens.clear();
    }

    @Override
    public void clearAll() {
        clearGames();
        clearAuthTokens();
        clearUsers();
    }

    @Override
    public void addGame(Game game, int gameID) {
        games.put(gameID, game);
    }

    @Override
    public void addUser(User user, String username) {
        users.put(username, user);
    }

    @Override
    public void addAuthToken(AuthToken authToken, String username) {
        authTokens.put(authToken, username);
    }

    @Override
    public Game getGame(int gameID) {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        }
        else {
            return null;
        }
    }

    @Override
    public User getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        else {
            return null;
        }
    }

    @Override
    public void renameGame(int gameID, String newName) {
        games.get(gameID).setGameName(newName);
    }

    public void removeGame(int gameID) {
        games.remove(gameID);
    }
}
