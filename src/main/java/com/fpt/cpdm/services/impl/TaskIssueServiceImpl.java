package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskIssueEntity;
import com.fpt.cpdm.exceptions.tasks.TaskIssueNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.forms.tasks.issues.TaskIssueForm;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;
import com.fpt.cpdm.repositories.TaskIssueRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.services.TaskIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskIssueServiceImpl implements TaskIssueService {

    private final TaskIssueRepository taskIssueRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskIssueServiceImpl(TaskIssueRepository taskIssueRepository, TaskRepository taskRepository) {
        this.taskIssueRepository = taskIssueRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskIssueDetail> readAll(Integer taskId) {

        List<TaskIssueDetail> taskIssueDetails = taskIssueRepository.findAllDetailByTask_IdAndAndAvailableTrue(taskId);
        return taskIssueDetails;
    }

    @Override
    public TaskIssueDetail create(Integer taskId, TaskIssueForm taskIssueForm) {

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(taskId)
        );

        // building entity
        TaskIssueEntity taskIssueEntity = TaskIssueEntity.builder()
                .summary(taskIssueForm.getSummary())
                .detail(taskIssueForm.getDetail())
                .weight(taskIssueForm.getWeight())
                .status("working")
                .task(taskEntity)
                .build();
        taskIssueEntity.setId(null);

        TaskIssueEntity savedTaskIssueEntity = taskIssueRepository.save(taskIssueEntity);
        TaskIssueDetail taskIssueDetail = taskIssueRepository.findDetailByIdAndAvailableTrue(savedTaskIssueEntity.getId()).orElseThrow(
                () -> new TaskIssueNotFoundException(savedTaskIssueEntity.getId())
        );

        return taskIssueDetail;
    }

    @Override
    public TaskIssueDetail update(Integer id, TaskIssueForm taskIssueForm) {

        TaskIssueEntity taskIssueEntity = taskIssueRepository.findById(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        taskIssueEntity.setDetail(taskIssueForm.getDetail());
        taskIssueEntity.setSummary(taskIssueForm.getSummary());
        taskIssueEntity.setWeight(taskIssueForm.getWeight());

        taskIssueRepository.save(taskIssueEntity);
        TaskIssueDetail savedTaskIssueDetail = taskIssueRepository.findDetailByIdAndAvailableTrue(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        return savedTaskIssueDetail;
    }

    @Override
    public void delete(Integer id) {

        TaskIssueEntity taskIssueEntity = taskIssueRepository.findById(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        taskIssueEntity.setAvailable(Boolean.FALSE);

        taskIssueRepository.save(taskIssueEntity);
    }
}
