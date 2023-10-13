package service.createClasses;

import server.models.AuthToken;

public class CreateRequest {
    private AuthToken authToken;
    private String gameName;

    public CreateRequest(AuthToken authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
