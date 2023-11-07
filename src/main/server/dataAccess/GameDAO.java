package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.Game;
import chess.ChessGame;

public class GameDAO implements DataAccessInterface{
    private DatabaseInterface database;

    public GameDAO(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public int insert(Game game) throws DataAccessException {
        if (game.getGameID() > 0) {
            throw new DataAccessException("game already added");
        }
        int gameID = database.getNewGameID();
        game.setGameID(gameID);
        database.addGame(game,gameID);
        return gameID;
    }

    @Override
    public Game find(int gameID) throws DataAccessException {
        if (database.getGame(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        return database.getGame(gameID);
    }

    @Override
    public Game[] findAll() {
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
                    Game tempGame = database.getGame(gameID);
                    tempGame.setWhiteUsername(username);
                    updateGame(gameID, tempGame);
                }
            }
            if (color == ChessGame.TeamColor.BLACK) {
                if (database.getGame(gameID).getBlackUsername() != null) {
                    throw new DataAccessException("Black Player already present in game " + gameID);
                }
                else {
                    Game tempGame = database.getGame(gameID);
                    tempGame.setBlackUsername(username);
                    updateGame(gameID, tempGame);
                }
            }
        }
    }

    @Override
    public void updateGame(int gameID, Game game) throws DataAccessException {
        if (database.getGame(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        else {
            database.updateGame(gameID, game);
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
