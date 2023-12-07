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
        ServerMessage serverMessage;
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> serverMessage = joinPlayer(userGameCommand.getGameID(), userGameCommand.getPlayerColor());
            case JOIN_OBSERVER -> serverMessage = joinObserver(userGameCommand.getGameID());
            case MAKE_MOVE -> serverMessage = makeMove(userGameCommand.getGameID(), userGameCommand.getChessMove());
            case LEAVE -> serverMessage = leave(userGameCommand.getGameID());
            case RESIGN -> serverMessage = resign(userGameCommand.getGameID());
        }
    }

    private ServerMessage joinPlayer(int gameID, ChessGame.TeamColor playerColor) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage joinObserver(int gameID) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage makeMove(int gameID, ChessMoveImpl move) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage leave(int gameID) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage resign(int gameID) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

}
