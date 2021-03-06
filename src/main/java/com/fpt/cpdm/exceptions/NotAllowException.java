package com.fpt.cpdm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowException extends RuntimeException {

    public NotAllowException(String message) {
        super(message, null, true, false);
    }

}
