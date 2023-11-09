package RequestResultClasses.registerClasses;

import models.AuthToken;

/**
 * result of request to register new user
 * includes username and authorization token of user registered
 * includes response code of success(200) or error code
 * includes error message if one is provided
 */
public class RegisterResult {
    private String username;
    private String message;
    private AuthToken authToken;
    private int responseCode;

    /**
     * constructor if successful
     * sets response code to 200
     * @param username of user registered
     * @param authToken of user registered
     */
    public RegisterResult(String username, AuthToken authToken) {
        this.username = username;
        this.authToken = authToken;
        responseCode = 200;
    }

    /**
     * constructor if error occurred
     * @param responseCode error code
     * @param message error message
     */
    public RegisterResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
