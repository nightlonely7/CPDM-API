package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface TaskService {

    TaskDetail findDetailById(User user, Integer id);

    TaskSummary changeStatus(Task task);

    TaskSummary save(Task task);

    Page<TaskSummary> findAllSummaryByExecutor(User user, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreator(User user, String title, String summary, Pageable pageable);

    void deleteById(Integer id);
}
