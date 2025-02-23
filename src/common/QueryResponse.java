package common;

public class QueryResponse {
    private final int responseCode;
    private final Object response;

    public QueryResponse(int responseCode, Object response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Object getResponse() {
        return response;
    }

}
