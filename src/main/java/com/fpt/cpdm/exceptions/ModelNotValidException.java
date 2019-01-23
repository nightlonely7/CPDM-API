package com.fpt.cpdm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ModelNotValidException extends RuntimeException {

    public ModelNotValidException() {
    }

    public ModelNotValidException(String message) {
        super(message, null, true, false);
    }

    public ModelNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelNotValidException(Throwable cause) {
        super(cause);
    }

    public ModelNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
