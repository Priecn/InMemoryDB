package exception;

public class DatabaseMangerException extends Throwable {

    public DatabaseMangerException(String message) {
        super(message);
    }

    public DatabaseMangerException(Throwable e, String message) {
        super(message, e);
    }

    public DatabaseMangerException(ErrorResponse errorResponse) {
        super(String.format("Exception occurred with ERR_CODE: %d and ERR_MSG: %s", errorResponse.getErrorCode(), errorResponse.getErrorMessage()));
    }
}
