package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.tasks.TaskUpdateForm;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.models.tasks.TaskBasic;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    void uploadFile(Integer id, String filename);

    TaskDetail findDetailById(Integer id);

    Page<TaskSummary> findAllSummaryByRelatives(Pageable pageable);

    TaskSummary changeStatus(Task task);

    TaskDetail create(TaskCreateForm taskCreateForm);

    TaskDetail update(Integer id, TaskUpdateForm taskUpdateForm);

    Page<TaskSummary> findAllSummaryByExecutor(String title, String summary, Integer projectId, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreator(String title, String summary, Integer projectId, Pageable pageable);

    void deleteById(Integer id);

    Page<TaskSummary> findAllByParentTask_Id(Integer taskId, Pageable pageable);

    List<TaskBasic> findAllBasicByCurrentExecutorAndProject_Id(Integer projectId);
}
