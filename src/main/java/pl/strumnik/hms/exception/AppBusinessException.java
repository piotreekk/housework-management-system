package pl.strumnik.hms.exception;

import lombok.Getter;

@Getter
public class AppBusinessException extends AppBaseRuntimeException {
    private ErrorCode errorCode;

    public AppBusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
