package com.fpt.cpdm.exceptions.users;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "User";

    public UserNotFoundException(Integer id) {
        super(id, ENTITY);
    }

}
