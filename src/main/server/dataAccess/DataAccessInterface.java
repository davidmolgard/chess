package server.dataAccess;

import dataAccess.DataAccessException;
import models.Game;
import chess.ChessGame;

/**
 * DataAccessInterface allows for data to be inserted and returned from the database
 * Implementations for games, users, and authorization tokens
 */
public interface DataAccessInterface {

    /**
     * Allows Data to be inserted to the database
     * @return ID number of the data inserted
     */
    int insert(Game game) throws DataAccessException;

    /**
     * @param gameID of game to find
     * @return Game if found
     * @throws DataAccessException if GameId is invalid/ game not found
     */
    Game find(int gameID) throws DataAccessException;

    /**
     * @return String representation of data type being accessed
     */
    Game[] findAll();

    /**
     * claims a spot in the game for the user provided
     * @param gameID of game to join
     * @param username of user looking to join the game
     * @throws DataAccessException if user or game not able to be found
     */
    void claimSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException;

    /**
     * renames a game
     * @param gameID to be renamed
     * @param game updated
     * @throws DataAccessException if game cannot be found
     */
    void updateGame(int gameID, Game game) throws DataAccessException;

    /**
     * removes a game
     * @param gameID of game to remove
     * @throws DataAccessException if game cannot be found
     */
    void removeGame(int gameID) throws DataAccessException;

    /**
     * clears all data in implementation
     */
    void clearGames();
}
