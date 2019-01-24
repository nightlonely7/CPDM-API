package com.fpt.cpdm.exceptions.users;

import com.fpt.cpdm.exceptions.EntityIdNotFoundException;

public class UserIdNotFoundException extends EntityIdNotFoundException {

    private static final String ENTITY = "User";

    public UserIdNotFoundException(Integer id) {
        super(id, ENTITY);
    }

}
