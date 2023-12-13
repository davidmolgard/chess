import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

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

    public void leave(String authToken) {

    }

    public void makeMove(String authToken, ChessMove move) {

    }

    public void resign(String authToken) {

    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        UserGameCommand userGameCommand = new UserGameCommand(authToken, gameID, playerColor);
        userGameCommand.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
        sendMessage(userGameCommand);
    }

    public void joinObserver(String authToken, int gameID) {
        UserGameCommand userGameCommand = new UserGameCommand(authToken, gameID);
        userGameCommand.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
        sendMessage(userGameCommand);
    }

    private void sendMessage(UserGameCommand userGameCommand) {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}