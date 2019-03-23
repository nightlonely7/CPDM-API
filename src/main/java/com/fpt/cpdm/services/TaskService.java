package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    void uploadFile(Integer id, String filename);

    TaskDetail findDetailById(Integer id);

    Page<TaskSummary> findAllSummaryByRelatives(Pageable pageable);

    TaskSummary changeStatus(Task task);

    TaskSummary save(Task task);

    TaskSummary create(TaskCreateForm taskCreateForm);

    Page<TaskSummary> findAllSummaryByExecutor(User user, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreator
            (User user, String title, String summary, Integer projectId, Pageable pageable);

    void deleteById(Integer id);

    Page<TaskSummary> findAllByParentTaskId(Integer taskId, Pageable pageable);
}
