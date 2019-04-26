package com.fpt.cpdm.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskHistoryEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.models.tasks.histories.TaskHistoryDataVersion1;
import com.fpt.cpdm.repositories.TaskHistoryRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.TaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public TaskHistoryServiceImpl(TaskHistoryRepository taskHistoryRepository, AuthenticationService authenticationService) {
        this.taskHistoryRepository = taskHistoryRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void save(TaskEntity taskEntity) {

        TaskHistoryDataVersion1 taskHistoryDataVersion1 = new TaskHistoryDataVersion1();
        taskHistoryDataVersion1.setTitle(taskEntity.getTitle());
        taskHistoryDataVersion1.setSummary(taskEntity.getSummary());
        taskHistoryDataVersion1.setDescription(taskEntity.getDescription());
        taskHistoryDataVersion1.setCreatedTime(taskEntity.getCreatedTime().toString());
        taskHistoryDataVersion1.setStartTime(taskEntity.getStartTime().toString());
        taskHistoryDataVersion1.setEndTime(taskEntity.getEndTime().toString());
        if (taskEntity.getCompletedTime() != null) {
            taskHistoryDataVersion1.setCompletedTime(taskEntity.getCompletedTime().toString());
        }
        taskHistoryDataVersion1.setCreatorId(taskEntity.getCreator().getId());
        taskHistoryDataVersion1.setExecutorId(taskEntity.getExecutor().getId());
        taskHistoryDataVersion1.setProjectId(taskEntity.getProject().getId());
        if (taskEntity.getParentTask() != null) {
            taskHistoryDataVersion1.setParentTaskId(taskEntity.getParentTask().getId());
        }
        taskHistoryDataVersion1.setPriority(taskEntity.getPriority());
        taskHistoryDataVersion1.setStatus(taskEntity.getStatus());
        taskHistoryDataVersion1.setAvailable(taskEntity.getAvailable());

        String data = "";
        try {
            data = new ObjectMapper().writeValueAsString(taskHistoryDataVersion1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TaskHistoryEntity taskHistoryEntity = new TaskHistoryEntity();
        taskHistoryEntity.setTask(taskEntity);
        taskHistoryEntity.setCreatedTime(taskEntity.getLastModifiedTime());
        taskHistoryEntity.setData(data);
        taskHistoryEntity.setCreator(authenticationService.getCurrentLoggedUser());
        taskHistoryRepository.save(taskHistoryEntity);
    }

    @Override
    public void findById(Integer id) {
        TaskHistoryEntity taskHistoryEntity = taskHistoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "TaskHistory")
        );
        TaskHistoryDataVersion1 taskHistoryDataVersion1 = null;
        try {
            taskHistoryDataVersion1 = new ObjectMapper()
                    .readValue(taskHistoryEntity.getData(), TaskHistoryDataVersion1.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(taskHistoryDataVersion1);
    }
}
