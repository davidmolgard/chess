package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;
import chess.ChessGame;

import java.util.HashMap;

public class GameDAO implements DataAccessInterface{
    private Database database = new InternalDatabase();
    public GameDAO() {

    }

    public GameDAO(Database database) {
        this.database = database;
    }

    @Override
    public int insert(Game game) {
        int gameID = database.getNewGameID();
        database.addGame(game,gameID);
        return gameID;
    }

    @Override
    public Game find(int gameID) throws DataAccessException {
        return database.getGame(gameID);
    }

    @Override
    public HashMap<Integer, Game> findAll() {
        return database.getGames();
    }

    @Override
    public void claimSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        if (database.getGame(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        else {
            if (color == ChessGame.TeamColor.WHITE) {
                if (database.getGame(gameID).getWhiteUsername() != null) {
                    throw new DataAccessException("White Player already present in game " + gameID);
                }
                else {
                    database.getGame(gameID).setWhiteUsername(username);
                }
            }
            if (color == ChessGame.TeamColor.BLACK) {
                if (database.getGame(gameID).getBlackUsername() != null) {
                    throw new DataAccessException("Black Player already present in game " + gameID);
                }
                else {
                    database.getGame(gameID).setBlackUsername(username);
                }
            }
        }
    }

    @Override
    public void updateGameName(int gameID, String newName) throws DataAccessException {
        if (database.getGame(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        else {
            database.renameGame(gameID, newName);
        }
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {
        if (database.getGame(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        else {
            database.removeGame(gameID);
        }
    }

    @Override
    public void clearGames() {
        database.clearGames();
    }
}
