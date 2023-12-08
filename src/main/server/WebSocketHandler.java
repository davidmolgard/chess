package server;

import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
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
            case MAKE_MOVE -> serverMessage = makeMove(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getChessMove(), session);
            case LEAVE -> serverMessage = leave(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
            case RESIGN -> serverMessage = resign(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
        }
    }

    private ServerMessage joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) {
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, getUsername(authToken)), playerColor, gameID));
        if (joinResult.getResponseCode() != services.OK) {
            return new ServerMessage(joinResult.getMessage(), ServerMessage.ServerMessageType.ERROR);
        }
        else {
            connections.add(authToken, gameID, true, session);
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    getUsername(authToken) + " joined the game as " + playerColor.toString() + " player.\n");
        }
    }

    private ServerMessage joinObserver(String authToken, int gameID, Session session) {
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, getUsername(authToken)), gameID));
        if (joinResult.getResponseCode() != services.OK) {
            return new ServerMessage(joinResult.getMessage(), ServerMessage.ServerMessageType.ERROR);
        }
        else {
            connections.add(authToken, gameID, false, session);
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    getUsername(authToken) + " joined the game as observer.\n");
        }
    }

    private ServerMessage makeMove(String authToken, int gameID, ChessMoveImpl move, Session session) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private ServerMessage leave(String authToken, int gameID, Session session) {
        LogoutResult logoutResult = services.logout(new LogoutRequest(new AuthToken(authToken, getUsername(authToken))));
        if (logoutResult.getResponseCode() != services.OK) {
            return new ServerMessage(logoutResult.getMessage(), ServerMessage.ServerMessageType.ERROR);
        }
        else {
            connections.remove(authToken);
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, getUsername(authToken) + " has left the game.\n");
        }
    }

    private ServerMessage resign(String authToken, int gameID, Session session) {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        return serverMessage;
    }

    private String getUsername(String authToken) {
        AuthToken authTokenTemp = new AuthToken();
        authTokenTemp.setAuthToken(authToken);
        return database.getUsername(authTokenTemp);
    }

}
