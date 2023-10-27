package service.listClasses;

import server.models.Game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * result of request to list games
 * includes response code of success(200) or error code
 * includes error message if one is provided, otherwise includes list of games as message
 */
public class ListResult {
    private int responseCode;
    private String message;

    private Game[] games;

    /**
     * constructor
     * @param responseCode error code
     * @param message error message
     */
    public ListResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public ListResult(Game[] games) {
        this.games = games;
        responseCode = 200;
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
