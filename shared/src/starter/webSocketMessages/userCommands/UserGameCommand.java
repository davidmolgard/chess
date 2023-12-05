package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMoveImpl;
import models.User;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public UserGameCommand(String authToken, int gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public UserGameCommand(String authToken, int gameID, ChessMoveImpl chessMove) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.chessMove = chessMove;
    }

    public UserGameCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    private int gameID = 0;

    private ChessMoveImpl chessMove = null;

    protected CommandType commandType;

    private ChessGame.TeamColor playerColor = null;

    private final String authToken;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public int getGameID() { return gameID; }

    public ChessMoveImpl getChessMove() { return chessMove; }

    public ChessGame.TeamColor getPlayerColor() { return playerColor; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
