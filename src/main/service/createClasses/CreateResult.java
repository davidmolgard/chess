package service.createClasses;

public class CreateResult {
    private String message;
    private int responseCode;
    private int GameID;

    public CreateResult(int gameID) {
        GameID = gameID;
        responseCode = 200;
    }

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
