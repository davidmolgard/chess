package server;

import chess.ChessGame;
import com.google.gson.Gson;
import server.dataAccess.Database;
import server.dataAccess.InternalDatabase;
import server.models.AuthToken;
import service.*;
import service.clearClasses.ClearRequest;
import service.clearClasses.ClearResult;
import service.createClasses.CreateRequest;
import service.createClasses.CreateResult;
import service.joinClasses.JoinRequest;
import service.joinClasses.JoinResult;
import service.listClasses.ListRequest;
import service.listClasses.ListResult;
import service.loginClasses.LoginRequest;
import service.loginClasses.LoginResult;
import service.logoutClasses.LogoutRequest;
import service.logoutClasses.LogoutResult;
import service.registerClasses.RegisterRequest;
import service.registerClasses.RegisterResult;
import spark.*;
import java.util.*;


public class Server {
    private Database database = new InternalDatabase();
    private Services services = new Services(database);

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("C:/Users/molga/chess/web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
    }

    private Object clear(Request req, Response res) {
        ClearResult clearResult = services.clear(new ClearRequest());
        res.type("application/json");
        clearResult.setMessage("test");
        if (clearResult.getResponseCode() == services.OK) {
            res.status(services.OK);
            return null;
        } else {
            res.status(clearResult.getResponseCode());
            return new Gson().toJson(Map.of("message", clearResult.getMessage()));
        }
    }

    private Object register(Request req, Response res) {
        res.type("application/json");
        var registerMap = getBody(req, Map.class);
        String username = (String) registerMap.get("username");
        String password = (String) registerMap.get("password");
        String email = (String) registerMap.get("email");
        if (username != null && password != null && email != null) {
            RegisterRequest registerRequest = new RegisterRequest(username, password, email);
            RegisterResult registerResult = services.register(registerRequest);
            res.status(registerResult.getResponseCode());
            if (registerResult.getResponseCode() == 200) {
                return new Gson().toJson(Map.of("username", registerResult.getUsername(), "authToken", registerResult.getAuthToken()));
            } else {
                return new Gson().toJson(Map.of("message", registerResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private Object login(Request req, Response res) {
        res.type("application/json");
        var loginMap = getBody(req, Map.class);
        String username = (String) loginMap.get("username");
        String password = (String) loginMap.get("password");
        if (username != null && password != null) {
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResult loginResult = services.login(loginRequest);
            res.status(loginResult.getResponseCode());
            if (loginResult.getResponseCode() == services.OK) {
                return new Gson().toJson(Map.of("username", loginResult.getUsername(), "authToken", loginResult.getAuthToken()));
            } else {
                return new Gson().toJson(Map.of("message", loginResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private Object logout(Request req, Response res) {
        res.type("application/json");
        String authToken = req.headers("authorization");
        if (authToken != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(authToken);
            LogoutRequest logoutRequest = new LogoutRequest(token);
            LogoutResult logoutResult = services.logout(logoutRequest);
            res.status(logoutResult.getResponseCode());
            if (logoutResult.getResponseCode() == services.OK) {
                return null;
            } else {
                return new Gson().toJson(Map.of("message", logoutResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");
        String authToken = req.headers("authorization");
        if (authToken != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(authToken);
            ListRequest listRequest = new ListRequest(token);
            ListResult listResult = services.list(listRequest);
            res.status(listResult.getResponseCode());
            if (listResult.getResponseCode() == services.OK) {
                return new Gson().toJson(Map.of("games", listResult.getGames()));
            } else {
                return new Gson().toJson(Map.of("message", listResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private Object createGame(Request req, Response res) {
        res.type("application/json");
        String authToken = req.headers("authorization");
        var bodyMap = getBody(req, Map.class);
        String gameName = (String) bodyMap.get("gameName");
        if (authToken != null && gameName != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(authToken);
            CreateRequest createRequest = new CreateRequest(token, gameName);
            CreateResult createResult = services.create(createRequest);
            res.status(createResult.getResponseCode());
            if (createResult.getResponseCode() == services.OK) {
                return new Gson().toJson(Map.of("gameID", createResult.getGameID()));
            }
            else {
                return new Gson().toJson(Map.of("message", createResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private Object joinGame(Request req, Response res) {
        res.type("application/json");
        String authToken = req.headers("authorization");
        var bodyMap = getBody(req, Map.class);
        String color = null;
        ChessGame.TeamColor teamColor = null;
        if (bodyMap.containsKey("playerColor")) {
            color = (String) bodyMap.get("playerColor");
            teamColor = ChessGame.TeamColor.valueOf(color);
        }
        double gameID = 0;
        int ID = 0;
        if (bodyMap.containsKey("gameID")) {
            gameID = (double) bodyMap.get("gameID");
            ID = (int) gameID;
        }
        if (authToken != null && gameID > 0) {
            AuthToken token = new AuthToken();
            token.setAuthToken(authToken);
            JoinRequest joinRequest = new JoinRequest(null, 0);
            if (color != null) {
                joinRequest = new JoinRequest(token, teamColor, ID);
            }
            else {
                joinRequest = new JoinRequest(token, ID);
            }
            JoinResult joinResult = services.join(joinRequest);
            res.status(joinResult.getResponseCode());
            if (joinResult.getResponseCode() == services.OK) {
                return res;
            }
            else {
                return new Gson().toJson(Map.of("message", joinResult.getMessage()));
            }
        }
        res.status(services.BadRequest);
        return new Gson().toJson(Map.of("message", "Error: bad request"));
    }

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

}
