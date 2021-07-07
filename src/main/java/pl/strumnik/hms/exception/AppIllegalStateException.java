package pl.strumnik.hms.exception;

public class AppIllegalStateException extends AppBaseRuntimeException {

    public AppIllegalStateException(String message) {
        super(message);
    }

    public AppIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
