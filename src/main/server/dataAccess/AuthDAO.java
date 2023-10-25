package server.dataAccess;

import server.models.AuthToken;

import java.util.UUID;

public class AuthDAO {
    private Database database = new InternalDatabase();

    public AuthDAO() {

    }

    public AuthDAO(Database database) {
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
}
