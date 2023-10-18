package service.logoutClasses;

import server.models.AuthToken;
import service.loginClasses.LoginRequest;

/**
 * request to logout user
 * includes Authorization token of user
 */
public class LogoutRequest {
    private AuthToken authToken;

    public LogoutRequest(AuthToken authToken) {
        this.authToken = authToken;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
