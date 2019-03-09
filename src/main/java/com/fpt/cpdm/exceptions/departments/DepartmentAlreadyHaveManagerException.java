package com.fpt.cpdm.exceptions.departments;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DepartmentAlreadyHaveManagerException extends RuntimeException {

    public DepartmentAlreadyHaveManagerException(String message) {
        super(message, null, true, false);
    }
}
