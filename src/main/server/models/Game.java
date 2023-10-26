package server.models;
import chess.ChessBoard;
import chess.ChessGame;

/**
 * Stores data for a single game
 * two users can play at once, one controlling white pieces, the other black
 * each game has a custom GameID and name
 */
public class Game {
    private int GameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName = "";
    private ChessGame game;
    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
