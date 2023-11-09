package RequestResultClasses.loginClasses;

import models.AuthToken;

/**
 * result of request to login user
 * includes username and Authorization token of user
 * includes response code of success(200) or error code
 * includes error message if one is provided
 */
public class LoginResult {
    private String message;
    private int responseCode;
    private String username;
    private AuthToken authToken;

    /**
     * constructor if error occurred
     * @param responseCode error code
     * @param errorMessage error message
     */
    public LoginResult(int responseCode, String errorMessage) {
        message = errorMessage;
        this.responseCode = responseCode;
    }

    /**
     * constructor if successful
     * @param username of user
     * @param authToken of user
     */
    public LoginResult(String username, AuthToken authToken) {
        this.username = username;
        this.authToken = authToken;
        responseCode = 200;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
