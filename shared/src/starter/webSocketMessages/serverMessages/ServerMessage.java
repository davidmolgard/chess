package webSocketMessages.serverMessages;

import models.Game;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public Game getGame() {
        return game;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    private Game game;
    private String errorMessage;
    private String message;

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage(ServerMessageType type, Game game) {
        this.serverMessageType = type;
        this.game = game;
    }

    public ServerMessage(ServerMessageType type, String message) {
        this.serverMessageType = type;
        this.message = message;
    }

    public ServerMessage(String errorMessage, ServerMessageType type) {
        this.errorMessage = errorMessage;
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
