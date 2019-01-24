package com.fpt.cpdm.exceptions.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailDuplicateException extends RuntimeException {

    private String email;

    public UserEmailDuplicateException(String email) {
        super(null, null, true, false);
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "User with email '" + email + "' is already existed!";
    }
}
