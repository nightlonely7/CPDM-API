package com.fpt.cpdm.exceptions.tasks;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Task";

    public TaskNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
