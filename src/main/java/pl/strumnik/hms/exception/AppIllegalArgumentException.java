package pl.strumnik.hms.exception;

public class AppIllegalArgumentException extends AppBaseRuntimeException {

    public AppIllegalArgumentException(String message) {
        super(message);
    }

    public AppIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
