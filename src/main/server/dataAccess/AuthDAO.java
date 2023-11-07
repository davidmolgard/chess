package server.dataAccess;

import server.models.AuthToken;

import java.util.UUID;

public class AuthDAO {
    private DatabaseInterface database;


    public AuthDAO(DatabaseInterface database) {
        this.database = database;
    }

    public boolean isAuthorized(AuthToken authToken) {
        return database.isAuthorized(authToken);
    }

    public AuthToken addAuthToken(String username) {
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken(UUID.randomUUID().toString());
        database.addAuthToken(authToken, username);
        return authToken;
    }

    public void removeAuthToken(AuthToken authToken) {
        database.removeAuthToken(authToken);
    }


    public String getUsername(AuthToken authToken) { return database.getUsername(authToken); }
}
