package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WebSocketServer {
    public static void main(String[] args) {
        WebSocketServer webSocketServer = new WebSocketServer();
        webSocketServer.run();
    }

    public void run() {
        Spark.port(8080);
        Spark.webSocket("/connect", WebSocketServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
        Spark.init();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("WebSocket response: " + message);
    }

}
