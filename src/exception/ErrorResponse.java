package exception;

public class ErrorResponse {
    private final int errorCode;
    private final String errorMessage;

    public ErrorResponse(int i, String s) {
        this.errorCode = i;
        this.errorMessage = s;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
