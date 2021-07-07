package pl.strumnik.hms.exception;

public class AppBaseRuntimeException extends RuntimeException {
    public AppBaseRuntimeException() {
    }

    public AppBaseRuntimeException(String message) {
        super(message);
    }

    public AppBaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppBaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public AppBaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
