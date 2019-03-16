package com.fpt.cpdm.exceptions.tasks;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class TaskIssueNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Task Issue";

    public TaskIssueNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
