package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;

import java.util.HashMap;

public class GameDataAccess implements DataAccessInterface {
    HashMap<Integer, Game> allGames = new HashMap<>();
    @Override
    public void insert() {
        int gameID = 0;
        allGames.put(gameID, new Game());
    }

    @Override
    public Game find(int gameID) throws DataAccessException {
        return allGames.get(gameID);
    }

    @Override
    public String findAll() {
        return allGames.toString();
    }

    @Override
    public void claimSpot(int gameID, String username) throws DataAccessException{

    }

    @Override
    public void updateGame(int gameID, String newName) throws DataAccessException{

    }

    @Override
    public void remove(int gameID) throws DataAccessException{
        allGames.remove(gameID);
    }

    @Override
    public void clear() {
        allGames.clear();
    }

}