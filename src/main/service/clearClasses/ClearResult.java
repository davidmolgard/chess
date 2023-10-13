package service.clearClasses;

public class ClearResult {
    private int responseCode;
    private String message;

    public ClearResult() {
        responseCode = 200;
    }
    public ClearResult(int responseCode, String message) {
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
