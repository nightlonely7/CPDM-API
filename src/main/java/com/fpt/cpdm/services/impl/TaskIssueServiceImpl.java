package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskIssueEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.ConflictException;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.tasks.TaskIssueNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.forms.tasks.issues.TaskIssueForm;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;
import com.fpt.cpdm.repositories.TaskIssueRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.TaskIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskIssueServiceImpl implements TaskIssueService {

    private final TaskIssueRepository taskIssueRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public TaskIssueServiceImpl(TaskIssueRepository taskIssueRepository, TaskRepository taskRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.taskIssueRepository = taskIssueRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<TaskIssueDetail> readAll(Integer taskId) {

        List<TaskIssueDetail> taskIssueDetails = taskIssueRepository.findAllDetailByTask_IdAndAvailableTrue(taskId);
        return taskIssueDetails;
    }

    @Override
    public TaskIssueDetail create(Integer taskId, TaskIssueForm taskIssueForm) {

        UserEntity creator = authenticationService.getCurrentLoggedUser();

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(taskId)
        );

        // check if issue's task is not currently running
        if (taskEntity.getStatus().equals("Working") == false
                && taskEntity.getStatus().equals("Outdated") == false
                && taskEntity.getStatus().equals("Near deadline") == false
                && taskEntity.getStatus().equals("Waiting") == false) {
            throw new ConflictException("This issue's task is not currently running");
        }

        // building entity
        TaskIssueEntity taskIssueEntity = TaskIssueEntity.builder()
                .summary(taskIssueForm.getSummary())
                .description(taskIssueForm.getDescription())
                .completed(Boolean.FALSE)
                .task(taskEntity)
                .creator(creator)
                .lastEditor(creator)
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

        UserEntity lastEditor = authenticationService.getCurrentLoggedUser();

        TaskIssueEntity taskIssueEntity = taskIssueRepository.findById(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        taskIssueEntity.setLastEditor(lastEditor);
        taskIssueEntity.setLastModifiedTime(LocalDateTime.now());
        taskIssueEntity.setDescription(taskIssueForm.getDescription());
        taskIssueEntity.setSummary(taskIssueForm.getSummary());

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

    @Override
    public TaskIssueDetail complete(Integer id) {

        UserEntity current = authenticationService.getCurrentLoggedUser();

        TaskIssueEntity taskIssueEntity = taskIssueRepository.findById(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        if (current.equals(taskIssueEntity.getTask().getExecutor()) == false) {
            throw new UnauthorizedException();
        }

        taskIssueEntity.setCompleted(Boolean.TRUE);
        taskIssueEntity.setCompletedTime(LocalDateTime.now());

        TaskIssueEntity savedTaskIssueEntity = taskIssueRepository.save(taskIssueEntity);
        TaskIssueDetail savedTaskIssueDetail = taskIssueRepository.findDetailByIdAndAvailableTrue(savedTaskIssueEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskIssueEntity.getId(), "Task Issue")
        );

        return savedTaskIssueDetail;
    }
}
