package com.fpt.cpdm.exceptions.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNameNotFoundException extends RuntimeException {

    private String roleName;

    public RoleNameNotFoundException(String roleName) {
        super(null, null, true, false);
        this.roleName = roleName;
    }

    @Override
    public String getMessage() {
        return "This role '" + roleName + "' is not found!";
    }
}
