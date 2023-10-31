package server.dataAccess;

import dataAccess.DataAccessException;
import server.models.User;

public class UserDAO {
    private DatabaseInterface database = new InternalDatabase();
    public UserDAO() {

    }

    public UserDAO(DatabaseInterface database) {
        this.database = database;
    }

    public void insertUser(User user, String username) {
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

    public String getUsers() {
        return database.getUsers();
    }

    public User getUser(String username) {
        return database.getUser(username);
    }

}
