package service.logoutClasses;

public class LogoutResult {
    private String message;
    private int responseCode;

    public LogoutResult() {
        message = null;
        responseCode = 200;
    }

    public LogoutResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
