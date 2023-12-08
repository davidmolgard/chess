package server;

import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessMoveImpl;
import com.google.gson.Gson;
import dataAccess.Database;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Database database = new Database();
    private final Services services = new Services(database);

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        ServerMessage serverMessage;
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> serverMessage = joinPlayer(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getPlayerColor(), session);
            case JOIN_OBSERVER -> serverMessage = joinObserver(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
            case MAKE_MOVE -> serverMessage = makeMove(userGameCommand.getGameID(), userGameCommand.getChessMove(), session);
            case LEAVE -> serverMessage = leave(userGameCommand.getGameID(), session);
            case RESIGN -> serverMessage = resign(userGameCommand.getGameID(), session);
        }
    }

    private ServerMessage joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) {
        AuthToken authTokenTemp = new AuthToken();
        authTokenTemp.setAuthToken(authToken);
        String username = database.getUsername(authTokenTemp);
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, username), playerColor, gameID));
        if (joinResult.getResponseCode() != services.OK) {
            return new ServerMessage("Error: could not join game", ServerMessage.ServerMessageType.ERROR);
        }
        else {
            connections.add(authToken, gameID, true, session);
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username + " joined the game as " + playerColor.toString() + " player.\n");
        }
    }

    private ServerMessage joinObserver(String authToken, int gameID, Session session) {
        AuthToken authTokenTemp = new AuthToken();
        authTokenTemp.setAuthToken(authToken);
        String username = database.getUsername(authTokenTemp);
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, username), gameID));
        if (joinResult.getResponseCode() != services.OK) {
            return new ServerMessage("Error: could not join game", ServerMessage.ServerMessageType.ERROR);
        }
        else {
            connections.add(authToken, gameID, false, session);
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username + " joined the game as observer.\n");
        }
    }

    private ServerMessage makeMove(int gameID, ChessMoveImpl move, Session session) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage leave(int gameID, Session session) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage resign(int gameID, Session session) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

}
