package RequestResultClasses.createClasses;

/**
 * result of request to create a new game
 * includes GameID of game created if successful
 * includes response code of success(200) or error code
 * includes error message if one is provided
 */
public class CreateResult {
    private String message;
    private int responseCode;
    private int GameID;

    /**
     * constructor if successful
     * @param gameID of game created
     */
    public CreateResult(int gameID) {
        GameID = gameID;
        responseCode = 200;
    }

    /**
     * constructor if error occurred
     * @param responseCode error code
     * @param message error message
     */
    public CreateResult(int responseCode, String message) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }
}
