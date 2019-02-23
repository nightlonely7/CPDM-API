package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;

import java.util.List;

public interface TaskService {

    TaskSummary save(Task task);

    List<TaskSummary> findAllSummary();

    List<TaskSummary> findAllSummaryByExecutor(User user);

    List<TaskSummary> findAllSummaryByCreator(User user);
}
