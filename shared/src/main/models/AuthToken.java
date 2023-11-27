package models;

/**
 * Authorization token to allow a user to access the database/use services
 */
public class AuthToken {
    private String authToken;
    private String username;

    public AuthToken() {

    }
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
