package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskSummary changeStatus(Task task);

    TaskSummary save(Task task);

    Page<TaskSummary> findAllSummary(Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutor(User user, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreator(User user, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndTitleContaining(User user, String title, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreatorAndTitleContaining(User user, String title, Pageable pageable);
}
