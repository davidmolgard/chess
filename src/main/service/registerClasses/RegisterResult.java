package service.registerClasses;

import server.models.AuthToken;

public class RegisterResult {
    private String username;
    private String message;
    private AuthToken authToken;
    private int responseCode;

    public RegisterResult(String username, AuthToken authToken) {
        this.username = username;
        this.authToken = authToken;
        responseCode = 200;
    }

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
