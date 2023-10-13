package service.joinClasses;
import chess.ChessGame;
import server.models.AuthToken;

public class JoinRequest {
    private AuthToken authToken;
    private ChessGame.TeamColor color = null;
    private int GameID;

    public JoinRequest(AuthToken authToken, ChessGame.TeamColor color, int gameID) {
        this.authToken = authToken;
        this.color = color;
        GameID = gameID;
    }

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
