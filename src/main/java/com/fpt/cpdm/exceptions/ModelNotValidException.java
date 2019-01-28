package com.fpt.cpdm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ModelNotValidException extends RuntimeException {

    public ModelNotValidException(String message) {
        super(message, null, true, false);
    }

}
