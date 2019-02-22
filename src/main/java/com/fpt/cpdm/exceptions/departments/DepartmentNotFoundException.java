package com.fpt.cpdm.exceptions.departments;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Department";

    public DepartmentNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
