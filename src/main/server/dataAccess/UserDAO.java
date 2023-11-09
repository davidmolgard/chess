package server.dataAccess;

import dataAccess.DataAccessException;
import models.User;

public class UserDAO {
    private DatabaseInterface database;

    public UserDAO(DatabaseInterface database) {
        this.database = database;
    }

    public void insertUser(User user, String username) throws DataAccessException {
        database.addUser(user, username);
    }

    public void deleteUser(String username) throws DataAccessException {
        if (database.getUser(username) == null) {
            throw new DataAccessException("User not found");
        }
        else {
            database.removeUser(username);
        }
    }

    public User getUser(String username) {
        return database.getUser(username);
    }

    public void clearUsers() {
        database.clearUsers();
    }

}
