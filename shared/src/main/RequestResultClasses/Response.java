package RequestResultClasses;

import com.google.gson.Gson;

public class Response {
    public Response(int responseCode, String message, Object body) {
        this.responseCode = responseCode;
        this.message = message;
        this.body = body;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    private int responseCode;
    private String message;
    private Object body;
}
