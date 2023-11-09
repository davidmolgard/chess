package RequestResultClasses.joinClasses;

/**
 * result of attempt to join a game
 * includes response code of success(200) or error code
 * includes error message if one is provided
 */
public class JoinResult {
    private int responseCode;
    private String message;

    /**
     * constructor if successful
     * sets response code to 200 to signal success
     */
    public JoinResult() {
        responseCode = 200;
    }
    /**
     * constructor if error occurred
     * @param responseCode error code
     * @param message error message
     */
    public JoinResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
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
