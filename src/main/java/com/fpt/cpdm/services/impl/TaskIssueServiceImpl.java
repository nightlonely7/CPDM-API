package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
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
import com.fpt.cpdm.services.TaskIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskIssueServiceImpl implements TaskIssueService {

    private final TaskIssueRepository taskIssueRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public TaskIssueServiceImpl(TaskIssueRepository taskIssueRepository, TaskRepository taskRepository, UserRepository userRepository, AuthenticationFacade authenticationFacade) {
        this.taskIssueRepository = taskIssueRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public List<TaskIssueDetail> readAll(Integer taskId) {

        List<TaskIssueDetail> taskIssueDetails = taskIssueRepository.findAllDetailByTask_IdAndAvailableTrue(taskId);
        return taskIssueDetails;
    }

    @Override
    public TaskIssueDetail create(Integer taskId, TaskIssueForm taskIssueForm) {

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

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity current = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        TaskIssueEntity taskIssueEntity = taskIssueRepository.findById(id).orElseThrow(
                () -> new TaskIssueNotFoundException(id)
        );

        if (current.equals(taskIssueEntity.getTask().getExecutor()) == false) {
            throw new UnauthorizedException();
        }

        taskIssueEntity.setCompleted(Boolean.TRUE);

        TaskIssueEntity savedTaskIssueEntity = taskIssueRepository.save(taskIssueEntity);
        TaskIssueDetail savedTaskIssueDetail = taskIssueRepository.findDetailByIdAndAvailableTrue(savedTaskIssueEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskIssueEntity.getId(), "Task Issue")
        );

        return savedTaskIssueDetail;
    }
}
