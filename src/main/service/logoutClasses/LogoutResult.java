package service.logoutClasses;

public class LogoutResult {
    String message;

    public LogoutResult() {
        message = null;
    }

    public LogoutResult(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
