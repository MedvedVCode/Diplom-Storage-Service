package medved.java.back.exception;

public class ActionDataException extends RuntimeException {
    public ActionDataException() {
    }

    public ActionDataException(String message) {
        super(message);
    }

    public ActionDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionDataException(Throwable cause) {
        super(cause);
    }

    public ActionDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
