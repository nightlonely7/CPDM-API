package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.TaskRelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskRelativeServiceImpl implements TaskRelativeService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskRelativeServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<UserSummary> readAll(Integer taskId) {

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(taskId)
        );

        List<UserSummary> userSummaries = userRepository.findAllSummaryByRelatedTasksAndEnabledTrue(taskEntity);

        return userSummaries;
    }

    @Override
    public List<UserSummary> add(Integer taskId, List<IdOnlyForm> relativesForm) {

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(taskId)
        );

        List<UserSummary> currentRelatives = userRepository.findAllSummaryByRelatedTasksAndEnabledTrue(taskEntity);

        List<UserEntity> relatives = new ArrayList<>();

        for (UserSummary userSummary : currentRelatives) {
            UserEntity relative = new UserEntity(userSummary.getId());
            relatives.add(relative);
        }

        for (IdOnlyForm idOnlyForm : relativesForm) {
            UserEntity relative = new UserEntity(idOnlyForm.getId());
            relatives.add(relative);
        }

        taskEntity.setRelatives(relatives.stream().distinct().collect(Collectors.toList()));
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        List<UserSummary> userSummaries = userRepository.findAllSummaryByRelatedTasksAndEnabledTrue(savedTaskEntity);

        return userSummaries;
    }

    @Override
    public void delete(Integer id) {

    }
}
