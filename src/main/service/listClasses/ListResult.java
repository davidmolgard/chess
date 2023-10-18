package service.listClasses;

/**
 * result of request to list games
 * includes response code of success(200) or error code
 * includes error message if one is provided, otherwise includes list of games as message
 */
public class ListResult {
    private int responseCode;
    private String message;

    /**
     * constructor
     * @param responseCode 200 if success, otherwise error code
     * @param message error message if provided, otherwise list of games
     */
    public ListResult(int responseCode, String message) {
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
