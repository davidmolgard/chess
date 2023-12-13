package server;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {


    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, int gameID, boolean isPlayer, Session session) {
        var connection = new Connection(authToken, gameID, isPlayer, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(int gameID, WebSocketHandler.ClientsToNotify clientsToNotify, String authToken, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Connection connection : connections.values()) {
            if (connection.session.isOpen()) {
                switch (clientsToNotify) {

                    case ALL -> {
                        if (connection.gameID == gameID) {
                            connection.send(serverMessage);
                        }
                    }
                    case OTHER -> {
                        if (connection.gameID == gameID && !connection.authToken.equals(authToken)) {
                            connection.send(serverMessage);
                        }
                    }
                    case THIS -> {
                        if (connection.authToken.equals(authToken)) {
                            connection.send(serverMessage);
                        }
                    }
                }

            } else {
                removeList.add(connection);
            }
        }

        // Clean up any connections that were left open.
        for (Connection connection : removeList) {
            connections.remove(connection.authToken);
        }
    }
}