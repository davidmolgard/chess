package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;

import java.util.HashMap;

public interface DataAccessInterface {
    /**
     * Allows Data to be inserted to the database
     * @return ID number of the data inserted
     */
    int insert();

    /**
     *
     * @param gameID
     * @return
     * @throws DataAccessException
     */
    Game find(int gameID) throws DataAccessException;

    String findAll();

    void claimSpot(int gameID, String username) throws DataAccessException;

    void updateGame(int gameID, String newName) throws DataAccessException;

    void remove(int gameID) throws DataAccessException;

    /**
     * clears all data in implementation
     */
    void clear();
}
