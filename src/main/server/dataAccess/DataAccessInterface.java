package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;

import java.util.HashMap;

public interface DataAccessInterface {
    void insert();

    Game find(int gameID) throws DataAccessException;

    String findAll();

    void claimSpot(int gameID, String username) throws DataAccessException;

    void updateGame(int gameID, String newName) throws DataAccessException;

    void remove(int gameID) throws DataAccessException;

    void clear();
}
