package com.fpt.cpdm.exceptions.tasks;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskTimeException extends RuntimeException {

    public TaskTimeException(String message) {
        super(message, null, true, false);
    }
}
