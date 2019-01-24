package com.fpt.cpdm.exceptions.tasks;

import com.fpt.cpdm.exceptions.EntityIdNotFoundException;

public class TaskIdNotFoundException extends EntityIdNotFoundException {

    private static final String ENTITY = "Task";

    public TaskIdNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
