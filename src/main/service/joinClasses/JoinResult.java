package service.joinClasses;

public class JoinResult {
    private int responseCode;
    private String message;

    public JoinResult() {
        responseCode = 200;
    }

    public JoinResult(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
