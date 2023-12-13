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

import java.io.IOException;

import static server.WebSocketHandler.ClientsToNotify.OTHER;
import static server.WebSocketHandler.ClientsToNotify.THIS;

@WebSocket
public class WebSocketHandler {
    public enum ClientsToNotify {
        ALL,
        OTHER,
        THIS
    }

    private final ConnectionManager connections = new ConnectionManager();
    private final Database database = new Database();
    private final Services services = new Services(database);

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getPlayerColor(), session);
            case JOIN_OBSERVER -> joinObserver(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
            case MAKE_MOVE -> makeMove(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getChessMove(), session);
            case LEAVE -> leave(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
            case RESIGN -> resign(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
        }
    }

    private void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) {
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, getUsername(authToken)), playerColor, gameID));
        if (joinResult.getResponseCode() != services.OK) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage(joinResult.getMessage(), ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            connections.add(authToken, gameID, true, session);
            try {
                connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        getUsername(authToken) + " joined the game as " + playerColor.toString() + " player.\n"));
                connections.broadcast(gameID, THIS, authToken, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, database.getGame(gameID)));
            }
            catch(IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    private void joinObserver(String authToken, int gameID, Session session) {
        JoinResult joinResult = services.join(new JoinRequest(new AuthToken(authToken, getUsername(authToken)), gameID));
        if (joinResult.getResponseCode() != services.OK) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage(joinResult.getMessage(), ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            connections.add(authToken, gameID, false, session);
            try {
                connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        getUsername(authToken) + " joined the game as observer.\n"));
                connections.broadcast(gameID, THIS, authToken, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, database.getGame(gameID)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void makeMove(String authToken, int gameID, ChessMoveImpl move, Session session) {

    }

    private void leave(String authToken, int gameID, Session session) {
        LogoutResult logoutResult = services.logout(new LogoutRequest(new AuthToken(authToken, getUsername(authToken))));
        if (logoutResult.getResponseCode() != services.OK) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage(logoutResult.getMessage(), ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Game game = database.getGame(gameID);
            if (game.getWhiteUsername().equals(getUsername(authToken))) {
                game.setWhiteUsername(null);
            }
            else if (game.getBlackUsername().equals(getUsername(authToken))) {
                game.setBlackUsername(null);
            }
            database.updateGame(gameID, game);
            connections.remove(authToken);
            try {
                connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, getUsername(authToken) + " has left the game.\n"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void resign(String authToken, int gameID, Session session) {

    }

    private String getUsername(String authToken) {
        AuthToken authTokenTemp = new AuthToken();
        authTokenTemp.setAuthToken(authToken);
        return database.getUsername(authTokenTemp);
    }

}
