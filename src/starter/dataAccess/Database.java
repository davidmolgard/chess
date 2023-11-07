package dataAccess;

import com.google.gson.Gson;
import server.dataAccess.DatabaseInterface;
import server.models.AuthToken;
import server.models.Game;
import server.models.User;

import java.sql.*;

/**
 * Responsible for creating connections to the database. Connections should be closed after use, either by calling
 * {@link #closeConnection(Connection)} on the Database instance or directly on the connection.
 */
public class Database implements DatabaseInterface {

    public static final String DB_NAME = "chess";
    private static final String DB_USERNAME = "Admin";
    private static final String DB_PASSWORD = "myD@vidis#1!";

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    private int gameIDGenerator = 1000;

    public Database() {
        InitializeDatabase();
    }

    /**
     * Gets a connection to the database.
     *
     * @return Connection the connection.
     * @throws DataAccessException if a data access error occurs.
     */
    public Connection getConnection() throws DataAccessException {
        try {
            return DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void InitializeDatabase() {
        try (Connection conn = getConnection()) {
            PreparedStatement createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            createDbStatement.executeUpdate();
            conn.setCatalog(DB_NAME);
            String createGameTable = """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL,
                game LONGTEXT NOT NULL,
                PRIMARY KEY (gameID)
            )""";
            String createUserTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";
            String createAuthTokenTable = """
            CREATE TABLE IF NOT EXISTS authTokens (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                authToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";
            try (PreparedStatement createGameTableStatement = conn.prepareStatement(createGameTable)) {
                createGameTableStatement.executeUpdate();
            }
            try (PreparedStatement createUserTableStatement = conn.prepareStatement(createUserTable)) {
                createUserTableStatement.executeUpdate();
            }
            try (PreparedStatement createAuthTokenTableStatement = conn.prepareStatement(createAuthTokenTable)) {
                createAuthTokenTableStatement.executeUpdate();
            }
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Closes the specified connection.
     *
     * @param connection the connection to be closed.
     * @throws DataAccessException if a data access error occurs while closing the connection.
     */
    public void closeConnection(Connection connection) throws DataAccessException {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    @Override
    public Game[] getGames() {
        return new Game[0];
    }

    @Override
    public String getUsers() {
        return null;
    }

    @Override
    public String getAuthTokens() {
        return null;
    }

    @Override
    public boolean isAuthorized(AuthToken authToken) {
        return false;
    }

    @Override
    public void clearGames() {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE IF EXISTS games")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void clearUsers() {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE IF EXISTS users")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void clearAuthTokens() {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE IF EXISTS authTokens")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void clearAll() {
        clearAuthTokens();
        clearGames();
        clearUsers();
    }

    @Override
    public void addGame(Game game, int gameID) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, game) VALUES(?, ?)")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, new Gson().toJson(game));
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void addUser(User user, String username) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void addAuthToken(AuthToken authToken, String username) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO authTokens (username, authToken) VALUES(?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken.getAuthToken());
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Game getGame(int gameID) {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public String getUsername(AuthToken authToken) {
        return null;
    }

    @Override
    public void removeGame(int gameID) {

    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void removeAuthToken(AuthToken authToken) {

    }

    @Override
    public void renameGame(int gameID, String newName) {

    }

    @Override
    public int getNewGameID() {
        gameIDGenerator++;
        return gameIDGenerator;
    }

    @Override
    public boolean hasAuthToken(String username) {
        return false;
    }
}