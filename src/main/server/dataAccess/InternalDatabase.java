package server.dataAccess;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;

import java.util.HashMap;
import java.util.Objects;

public class InternalDatabase implements DatabaseInterface {
    private HashMap<Integer, Game> games = new HashMap<>();
    private HashMap<String, String> authTokens = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private int gameIDGenerator = 1000;
    @Override
    public Game[] getGames() {
        Game[] gamesArray = new Game[games.size()];
        int i = 0;
        for (int key : games.keySet()) {
            games.get(key).setGameID(key);
            gamesArray[i] = (games.get(key));
            i++;
        }
        return gamesArray;
    }

    public String getUsers() {
        return users.toString();
    }

    public String getAuthTokens() {
        return authTokens.toString();
    }

    @Override
    public boolean isAuthorized(AuthToken authToken) {
        return authTokens.containsKey(authToken.getAuthToken());
    }

    @Override
    public void removeAuthToken(AuthToken authToken) {
        authTokens.remove(authToken.getAuthToken());
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
        game.setGameID(gameID);
        games.put(gameID, game);
    }

    @Override
    public void addUser(User user, String username) throws DataAccessException {
        if (!Objects.equals(user.getUsername(), username)) {
            throw new DataAccessException("Username did not match user");
        }
        users.put(username, user);
    }

    @Override
    public void addAuthToken(AuthToken authToken, String username) {
        authTokens.put(authToken.getAuthToken(), username);
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
    public void updateGame(int gameID, Game game) {
        games.replace(gameID, game);
    }

    public void removeGame(int gameID) {
        games.remove(gameID);
    }

    @Override
    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public int getNewGameID() {
        gameIDGenerator++;
        return gameIDGenerator;
    }

    @Override
    public boolean hasAuthToken(String username) {
        return authTokens.containsValue(username);
    }

    @Override
    public String getUsername(AuthToken authToken) { return authTokens.get(authToken.getAuthToken()); }
}
