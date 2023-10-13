package service.loginClasses;

import server.models.AuthToken;

public class LoginResult {
    private String message;
    private int responseCode;
    private String username;
    private AuthToken authToken;

    public LoginResult(int responseCode, String errorMessage) {
        message = errorMessage;
        this.responseCode = responseCode;
    }

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
