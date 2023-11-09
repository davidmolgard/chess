package RequestResultClasses.joinClasses;
import chess.ChessGame;
import models.AuthToken;

/**
 * request to join a game
 * includes Authorization token of user attempting to join and gameID of game to join
 * allows color to be provided or not
 */
public class JoinRequest {
    private AuthToken authToken;
    private ChessGame.TeamColor color = null;
    private int GameID;

    /**
     * constructor if color is provided
     * @param authToken of user
     * @param color requesting to play as
     * @param gameID of game requesting to join
     */
    public JoinRequest(AuthToken authToken, ChessGame.TeamColor color, int gameID) {
        this.authToken = authToken;
        this.color = color;
        GameID = gameID;
    }

    /**
     * constructor if color not provided
     * @param authToken of user
     * @param gameID of game requesting to join
     */
    public JoinRequest(AuthToken authToken, int gameID) {
        this.authToken = authToken;
        GameID = gameID;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.color = color;
    }

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }
}
