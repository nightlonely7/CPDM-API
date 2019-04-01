package com.fpt.cpdm.exceptions.assignRequests;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class AssignRequestNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "AssignRequest";

    public AssignRequestNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
