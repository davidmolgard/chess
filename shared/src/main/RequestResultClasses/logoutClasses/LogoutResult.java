package RequestResultClasses.logoutClasses;

/**
 * result of request to logout
 * includes response code of success(200) or error code
 * includes error message if one is provided
 */
public class LogoutResult {
    private String message;
    private int responseCode;

    /**
     * constructor if successful
     * sets response code to 200
     */
    public LogoutResult() {
        message = null;
        responseCode = 200;
    }

    /**
     * constructor if error occurred
     * @param responseCode error code
     * @param message error message
     */
    public LogoutResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
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

}
