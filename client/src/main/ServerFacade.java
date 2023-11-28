import RequestResultClasses.Response;
import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.listClasses.ListResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import chess.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.AuthToken;
import models.Game;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ServerFacade {
    private GsonBuilder gameBuilder = new GsonBuilder();

    private static HttpURLConnection sendRequest(String url, String method, String body, AuthToken authToken) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        writeRequestBody(body, http, authToken);
        http.connect();
        //System.out.printf("= Request =========\n[%s] %s\n\n%s\n\n", method, url, body);
        return http;
    }

    private static void writeRequestBody(String body, HttpURLConnection http, AuthToken authToken) throws IOException {
        if (authToken != null) {
            http.setDoOutput(true);
            http.setRequestProperty("authorization", authToken.getAuthToken());
        }
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static Response receiveResponse(HttpURLConnection http) throws IOException {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();

        Object responseBody = readResponseBody(http);
        //System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, responseBody);
        return new Response(statusCode, statusMessage, responseBody);
    }

    private static Object readResponseBody(HttpURLConnection http) throws IOException {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }
    public LoginResult login(LoginRequest loginRequest) {
        Response loginResponse = null;
        int responseCode = 500;
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/session", "POST",
                    Map.of("username", loginRequest.getUsername(), "password", loginRequest.getPassword()).toString(), null);
            loginResponse = receiveResponse(http);
        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch (Exception ex) {
            System.out.print("Exception " + ex.getClass() + ex.getMessage() + " caught in register method\n");
        }
        if (loginResponse == null) {
            if (responseCode == 500) {
                return new LoginResult(responseCode, "Error: server error");
            }
            else {
                return new LoginResult(responseCode, "Error: unauthorized");
            }
        }
        else {
            Map<String, String> responseMap = (Map<String, String>) loginResponse.getBody();
            String username = responseMap.get("username");
            String authToken = responseMap.get("authToken");
            return new LoginResult(username, new AuthToken(authToken, username));
        }
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        Response registerResponse = null;
        int responseCode = 500;
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/user", "POST",
                    Map.of("username", registerRequest.getUsername(), "password", registerRequest.getPassword(), "email", registerRequest.getEmail()).toString(), null);
            registerResponse = receiveResponse(http);
        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch(Exception ex) {
            System.out.print("Exception " + ex.getMessage() + " caught in register method\n");
        }
        if (registerResponse == null) {
            if (responseCode == 500) {
                return new RegisterResult(responseCode, "Error: server error");
            }
            else if (responseCode == 403) {
                return new RegisterResult(responseCode, "Error: already taken");
            }
            else {
                return new RegisterResult(responseCode, "Error: bad request");
            }
        }
        else {
            Map<String, String> responseMap = (Map<String, String>) registerResponse.getBody();
            String username = responseMap.get("username");
            String authToken = responseMap.get("authToken");
            return new RegisterResult(username, new AuthToken(authToken, username));
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) {
        Response logoutResponse = null;
        int responseCode = 500;
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/session", "DELETE", "", logoutRequest.getAuthToken());
            logoutResponse = receiveResponse(http);
        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch(Exception ex) {
            System.out.print("Exception " + ex.getClass() + ex.getMessage() + " caught in logout method\n");
        }
        if (logoutResponse == null) {
            if (responseCode == 500) {
                return new LogoutResult(responseCode, "Error: server error");
            }
            else {
                return new LogoutResult(responseCode, "Error: unauthorized");
            }
        }
        else {
            return new LogoutResult();
        }
    }

    public ListResult list(AuthToken authToken) {
        Response listResponse = null;
        int responseCode = 500;
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/game", "GET", "", authToken);
            if (http.getResponseCode() == 200) {
                Map<String, Game[]> responseMap;
                Type mapType = new TypeToken<Map<String, Game[]>>() {}.getType();
                createGameBuilder();
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseMap = gameBuilder.create().fromJson(inputStreamReader, mapType);
                    return new ListResult(responseMap.get("games"));
                }
            }
            else {
                responseCode = http.getResponseCode();
            }
        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch(Exception ex) {
            System.out.print("Exception " + ex.getClass() + ex.getMessage() + " caught in list method\n");
            System.out.print(Arrays.toString(ex.getStackTrace()));
        }
        if (listResponse == null) {
            if (responseCode == 500) {
                return new ListResult(responseCode, "Error: server error");
            }
            else {
                return new ListResult(responseCode, "Error: unauthorized");
            }
        }
        else {
            return new ListResult(500, "Error: server error");
        }
    }

    public CreateResult create(CreateRequest createRequest) {
        Response createResponse = null;
        int responseCode = 500;
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/game", "POST",
                    Map.of("gameName", createRequest.getGameName()).toString(), createRequest.getAuthToken());
            createResponse = receiveResponse(http);
        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch(Exception ex) {
            System.out.print("Exception " + ex.getClass() + ex.getMessage() + " caught in create method\n");
        }
        if (createResponse == null) {
            if (responseCode == 500) {
                return new CreateResult(responseCode, "Error: server error");
            }
            else if (responseCode == 401){
                return new CreateResult(responseCode, "Error: unauthorized");
            }
            else {
                return new CreateResult(responseCode, "Error: bad request");
            }
        }
        else {
            Map<String, Double> responseMap = (Map<String, Double>) createResponse.getBody();
            return new CreateResult(responseMap.get("gameID").intValue());
        }
    }

    public JoinResult join(JoinRequest joinRequest) {
        Response joinResponse = null;
        int responseCode = 500;
        try {
            String color = "";
            if (joinRequest.getColor() != null) {
                color = joinRequest.getColor().toString();
                HttpURLConnection http = sendRequest("http://localhost:8080/game", "PUT",
                        Map.of("playerColor", color, "gameID", joinRequest.getGameID()).toString(), joinRequest.getAuthToken());
                joinResponse = receiveResponse(http);
            }
            else {
                HttpURLConnection http = sendRequest("http://localhost:8080/game", "PUT",
                        Map.of( "gameID", joinRequest.getGameID()).toString(), joinRequest.getAuthToken());
                joinResponse = receiveResponse(http);
            }


        } catch(IOException ex) {
            responseCode = getErrorCode(ex);
        }
        catch(Exception ex) {
            System.out.print("Exception " + ex.getClass() + ex.getMessage() + " caught in join method\n");
        }
        if (joinResponse == null) {
            if (responseCode == 500) {
                return new JoinResult(responseCode, "Error: server error");
            }
            else if (responseCode == 401) {
                return new JoinResult(responseCode, "Error: unauthorized");
            }
            else if (responseCode == 403) {
                return new JoinResult(responseCode, "Error: already taken");
            }
            else {
                return new JoinResult(responseCode, "Error: bad request");
            }
        }
        else {
            return new JoinResult();
        }
    }

    public void clear() {
        try {
            HttpURLConnection http = sendRequest("http://localhost:8080/db", "DELETE", "", null);
            receiveResponse(http);
        } catch(Exception ex) {
            System.out.print("Exception " + ex.getMessage() + " caught in register method\n");
        }

    }

    private int getErrorCode(IOException ex) {
        int errorCode = Integer.parseInt(ex.getMessage().replaceAll("[^0-9]", ""));
        errorCode = errorCode / 10000;
        return errorCode;
    }

    public class GameAdapter implements JsonDeserializer<ChessGame> {
        @Override
        public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());
            return builder.create().fromJson(jsonElement, ChessGameImpl.class);
        }
    }

    public class BoardAdapter implements JsonDeserializer<ChessBoard> {

        @Override
        public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
            return builder.create().fromJson(jsonElement, ChessBoardImpl.class);
        }
    }

    public class PieceAdapter implements JsonDeserializer<ChessPiece> {

        @Override
        public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Gson().fromJson(jsonElement, ChessPieceImpl.class);
        }
    }
    private void createGameBuilder() {
        gameBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        gameBuilder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());
        gameBuilder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
    }

}
