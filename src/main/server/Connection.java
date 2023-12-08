package server;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String authToken;
    public int gameID;
    public boolean isPlayer;
    public Session session;


    public Connection(String authToken, int gameID, boolean isPlayer, Session session) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.isPlayer = isPlayer;
        this.session = session;
    }

    public void send(ServerMessage serverMessage) throws IOException {
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> session.getRemote().sendString("temp");//session.getRemote().sendBytes(serverMessage.getGame().);
            case ERROR -> session.getRemote().sendString(serverMessage.getErrorMessage());
            case NOTIFICATION -> session.getRemote().sendString(serverMessage.getMessage());
        }

    }
}