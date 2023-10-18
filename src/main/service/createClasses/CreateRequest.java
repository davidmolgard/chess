package service.createClasses;

import server.models.AuthToken;

/**
 * Request to create a game
 * includes name of game to be created and authorization token
 */
public class CreateRequest {
    private AuthToken authToken;
    private String gameName;

    /**
     * constructor
     * @param authToken authorization token of user making request
     * @param gameName of game to be created
     */
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
