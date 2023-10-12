package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;

import java.util.HashMap;

public class DataAccess {
    HashMap<Integer, Game> allGames = new HashMap<>();
    public Game insertGame() {
        int gameID = 0;
        allGames.put(gameID, new Game());
        return allGames.get(gameID);
    }

    public Game findGame(int gameID) throws DataAccessException {
        return allGames.get(gameID);
    }

    public HashMap<Integer, Game> findAllGames() {
        return allGames;
    }

    public void claimSpot(int gameID, String username) throws DataAccessException{

    }

    public void updateGame(int gameID, String newName) throws DataAccessException{

    }

    public void remove(int gameID) throws DataAccessException{
        allGames.remove(gameID);
    }

    public void clear() {
        allGames.clear();
    }

}