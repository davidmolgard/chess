import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageObserver serverMessageObserver;


    public WebSocketFacade(ServerMessageObserver serverMessageObserver) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.serverMessageObserver = serverMessageObserver;

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                Gson deserializer = ServerFacade.createGsonDeserializer();
                ServerMessage serverMessage = deserializer.fromJson(message, ServerMessage.class);
                serverMessageObserver.notify(serverMessage);
            }
        });
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}