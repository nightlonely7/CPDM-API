package com.fpt.cpdm.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserNotValidException extends RuntimeException {

    public UserNotValidException() {
    }

    public UserNotValidException(String message) {
        super(message);
    }

    public UserNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotValidException(Throwable cause) {
        super(cause);
    }

    public UserNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
