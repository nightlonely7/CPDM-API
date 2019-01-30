package com.fpt.cpdm.exceptions.roles;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class RoleNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Role";

    public RoleNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
