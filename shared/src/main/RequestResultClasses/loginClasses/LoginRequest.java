package RequestResultClasses.loginClasses;

/**
 * requests login of a user
 * includes username and password of user requesting login
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * constructor
     * @param username of user
     * @param password of user
     */
    public LoginRequest(String username, String password) {
        setUsername(username);
        setPassword(password);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
