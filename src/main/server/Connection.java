package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.nio.ByteBuffer;

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
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }
}