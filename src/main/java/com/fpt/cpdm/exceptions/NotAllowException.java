package com.fpt.cpdm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowException extends RuntimeException {

    public NotAllowException() {
    }

    public NotAllowException(String message) {
        super(message, null, true, false);
    }

    public NotAllowException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowException(Throwable cause) {
        super(cause);
    }

    public NotAllowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
