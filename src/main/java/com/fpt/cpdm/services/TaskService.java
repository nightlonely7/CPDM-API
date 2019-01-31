package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;

import java.util.List;

public interface TaskService extends CRUDService<Task> {
    List<TaskSummary> findAllSummary();
}
