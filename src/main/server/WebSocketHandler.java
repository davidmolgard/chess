package server;

import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dataAccess.Database;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import java.io.IOException;

import static server.WebSocketHandler.ClientsToNotify.*;

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
        Gson deserializer = createGsonDeserializer();
        UserGameCommand userGameCommand = deserializer.fromJson(message, UserGameCommand.class);
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken(userGameCommand.getAuthString());
        if (database.isAuthorized(authToken)) {
            switch (userGameCommand.getCommandType()) {
                case JOIN_PLAYER -> joinPlayer(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getPlayerColor(), session);
                case JOIN_OBSERVER -> joinObserver(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
                case MAKE_MOVE -> makeMove(userGameCommand.getAuthString(), userGameCommand.getGameID(), userGameCommand.getMove(), session);
                case LEAVE -> leave(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
                case RESIGN -> resign(userGameCommand.getAuthString(), userGameCommand.getGameID(), session);
            }
        }
        else {
            session.getRemote().sendString(new Gson().toJson(new ServerMessage("Error: Not authorized\n", ServerMessage.ServerMessageType.ERROR)));
        }
    }

    private void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) {
        if (database.getGame(gameID) == null) {
            try {
                session.getRemote().sendString(new Gson().toJson(new ServerMessage("Error: Game not found\n", ServerMessage.ServerMessageType.ERROR)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            boolean correctTeam = false;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                if (database.getGame(gameID).getWhiteUsername() != null) {
                    if (database.getGame(gameID).getWhiteUsername().equals(getUsername(authToken))) {
                        correctTeam = true;
                    }
                }
            }
            else if (playerColor == ChessGame.TeamColor.BLACK) {
                if (database.getGame(gameID).getBlackUsername() != null) {
                    if (database.getGame(gameID).getBlackUsername().equals(getUsername(authToken))) {
                        correctTeam = true;
                    }
                }
            }
            if (correctTeam) {
                connections.add(authToken, gameID, true, session);
                try {
                    connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            getUsername(authToken) + " joined the game as " + playerColor.toString() + " player.\n"));
                    connections.broadcast(gameID, THIS, authToken, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, database.getGame(gameID)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                try {
                    session.getRemote().sendString(new Gson().toJson(new ServerMessage("Error: try to join again.\n", ServerMessage.ServerMessageType.ERROR)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private void joinObserver(String authToken, int gameID, Session session) {

        if (database.getGame(gameID) == null) {
            try {
                session.getRemote().sendString( new Gson().toJson(new ServerMessage("Error: Game not found.\n", ServerMessage.ServerMessageType.ERROR)));
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

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) {
        Game game = database.getGame(gameID);
        if (game.isOver()) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage( "Error: Game is over.\n", ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (!connections.connections.get(authToken).isPlayer) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage( "Error: Observer cannot make moves.\n", ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            ChessGame.TeamColor color = null;
            if (game.getWhiteUsername() != null) {
                if (game.getWhiteUsername().equals(getUsername(authToken))) {
                    color = ChessGame.TeamColor.WHITE;
                }
            }
            else if (game.getBlackUsername() != null) {
                if (game.getBlackUsername().equals(getUsername(authToken))) {
                    color = ChessGame.TeamColor.BLACK;
                }
            }
            ChessPiece piece = game.getGame().getBoard().getPiece(move.getStartPosition());
            boolean invalidMove = false;
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    invalidMove = true;
                    try {
                        connections.broadcast(gameID, THIS, authToken, new ServerMessage("Error: Not your turn.\n", ServerMessage.ServerMessageType.ERROR));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!invalidMove) {
                try {
                    game.getGame().makeMove(move);
                    database.updateGame(gameID, game);
                    connections.broadcast(gameID, ALL, authToken, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));
                    connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, getUsername(authToken) + " made move: " + move.toString()));
                } catch (InvalidMoveException e) {
                    try {
                        connections.broadcast(gameID, THIS, authToken, new ServerMessage("Error: Invalid move.\n", ServerMessage.ServerMessageType.ERROR));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
            String notificationString = getUsername(authToken) + " has left the game.\n";
            connections.remove(authToken);
            try {
                connections.broadcast(gameID, OTHER, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationString));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void resign(String authToken, int gameID, Session session) {
        Game game = database.getGame(gameID);
        if (!connections.connections.get(authToken).isPlayer) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage("Error: Observers cannot resign\n", ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (game.isOver()) {
            try {
                connections.broadcast(gameID, THIS, authToken, new ServerMessage("Error: Game is already over\n", ServerMessage.ServerMessageType.ERROR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            game.setOver(true);
            database.updateGame(gameID, game);
            try {
                connections.broadcast(gameID, ALL, authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        getUsername(authToken) + " has resigned.\n"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getUsername(String authToken) {
        AuthToken authTokenTemp = new AuthToken();
        authTokenTemp.setAuthToken(authToken);
        return database.getUsername(authTokenTemp);
    }

    public static Gson createGsonDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImpl.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImpl.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImpl.class));


        return gsonBuilder.create();
    }

}
