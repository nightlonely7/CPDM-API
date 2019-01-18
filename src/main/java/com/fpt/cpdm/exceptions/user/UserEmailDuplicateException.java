package com.fpt.cpdm.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailDuplicateException extends RuntimeException {

    public UserEmailDuplicateException() {
    }

    public UserEmailDuplicateException(String message) {
        super(message);
    }

    public UserEmailDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailDuplicateException(Throwable cause) {
        super(cause);
    }

    public UserEmailDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
