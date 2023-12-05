package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessMoveImpl;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(userGameCommand.getGameID(), userGameCommand.getPlayerColor());
            case JOIN_OBSERVER -> joinObserver(userGameCommand.getGameID());
            case MAKE_MOVE -> makeMove(userGameCommand.getGameID(), userGameCommand.getChessMove());
            case LEAVE -> leave(userGameCommand.getGameID());
            case RESIGN -> resign(userGameCommand.getGameID());
        }
    }

    private void joinPlayer(int gameID, ChessGame.TeamColor playerColor) {

    }

    private void joinObserver(int gameID) {

    }

    private void makeMove(int gameID, ChessMoveImpl move) {

    }

    private void leave(int gameID) {

    }

    private void resign(int gameID) {

    }

}
