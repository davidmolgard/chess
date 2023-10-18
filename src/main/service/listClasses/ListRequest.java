package service.listClasses;

import server.models.AuthToken;

/**
 * request for a list of games
 * includes Authorization token of user making request
 */
public class ListRequest {
    private AuthToken authToken;

    /**
     * constructor
     * @param authToken of user
     */
    public ListRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
