package service.registerClasses;

/**
 * registers new user in database
 * includes username, password, and email of new user to be registered
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;

    /**
     * constructor
     * @param username of user
     * @param password of user
     * @param email of user
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
