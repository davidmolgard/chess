package service.listClasses;

import server.models.AuthToken;

public class ListRequest {
    private AuthToken authToken;
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
