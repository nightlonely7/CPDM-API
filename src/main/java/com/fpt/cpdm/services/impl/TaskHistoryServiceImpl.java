package com.fpt.cpdm.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskHistoryEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.tasks.histories.TaskHistoryData;
import com.fpt.cpdm.models.tasks.histories.TaskHistoryDataResponse;
import com.fpt.cpdm.models.tasks.histories.TaskHistoryResponse;
import com.fpt.cpdm.models.tasks.histories.TaskHistorySummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.repositories.TaskHistoryRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.TaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public TaskHistoryServiceImpl(TaskHistoryRepository taskHistoryRepository, ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository, AuthenticationService authenticationService) {
        this.taskHistoryRepository = taskHistoryRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void save(TaskEntity taskEntity) {

        TaskHistoryData taskHistoryData = new TaskHistoryData();
        taskHistoryData.setTitle(taskEntity.getTitle());
        taskHistoryData.setSummary(taskEntity.getSummary());
        taskHistoryData.setDescription(taskEntity.getDescription());
        taskHistoryData.setCreatedTime(taskEntity.getCreatedTime().toString());
        taskHistoryData.setStartTime(taskEntity.getStartTime().toString());
        taskHistoryData.setEndTime(taskEntity.getEndTime().toString());
        if (taskEntity.getCompletedTime() != null) {
            taskHistoryData.setCompletedTime(taskEntity.getCompletedTime().toString());
        }
        taskHistoryData.setCreatorId(taskEntity.getCreator().getId());
        taskHistoryData.setExecutorId(taskEntity.getExecutor().getId());
        taskHistoryData.setProjectId(taskEntity.getProject().getId());
        if (taskEntity.getParentTask() != null) {
            taskHistoryData.setParentTaskId(taskEntity.getParentTask().getId());
        }
        taskHistoryData.setPriority(taskEntity.getPriority());
        taskHistoryData.setStatus(taskEntity.getStatus());
        taskHistoryData.setAvailable(taskEntity.getAvailable());

        String data = "";
        try {
            data = new ObjectMapper().writeValueAsString(taskHistoryData);
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
    public List<TaskHistorySummary> findAllSummaryByTask_Id(Integer taskId) {

        List<TaskHistorySummary> taskHistorySummaries = taskHistoryRepository.findAllSummaryByTask_Id(taskId);

        return taskHistorySummaries;
    }

    @Override
    public TaskHistoryResponse findById(Integer id) {
        TaskHistoryEntity taskHistoryEntity = taskHistoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "TaskHistory")
        );

        TaskHistoryResponse taskHistoryResponse = new TaskHistoryResponse();
        taskHistoryResponse.setId(taskHistoryEntity.getId());
        UserDisplayName historyCreator = userRepository.findDisplayNameById(taskHistoryEntity.getCreator().getId()).orElseThrow(
                () -> new EntityNotFoundException(taskHistoryEntity.getCreator().getId(), "User")
        );
        taskHistoryResponse.setCreator(historyCreator);
        taskHistoryResponse.setCreatedTime(taskHistoryEntity.getCreatedTime());

        TaskSummary historyTask = taskRepository.findSummaryById(taskHistoryEntity.getTask().getId()).orElseThrow(
                () -> new EntityNotFoundException(taskHistoryEntity.getTask().getId(), "Task")
        );
        taskHistoryResponse.setTask(historyTask);

        TaskHistoryData taskHistoryData;
        try {
            taskHistoryData = new ObjectMapper().readValue(taskHistoryEntity.getData(), TaskHistoryData.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't read history data in Task History");
        }

        TaskHistoryDataResponse taskHistoryDataResponse = new TaskHistoryDataResponse();

        taskHistoryDataResponse.setTitle(taskHistoryData.getTitle());
        taskHistoryDataResponse.setSummary(taskHistoryData.getSummary());
        taskHistoryDataResponse.setDescription(taskHistoryData.getDescription());
        taskHistoryDataResponse.setCreatedTime(
                LocalDateTime.parse(taskHistoryData.getCreatedTime(), DateTimeFormatter.ISO_DATE_TIME));
        taskHistoryDataResponse.setStartTime(
                LocalDateTime.parse(taskHistoryData.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        taskHistoryDataResponse.setEndTime(
                LocalDateTime.parse(taskHistoryData.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        if (taskHistoryData.getCompletedTime() != null) {
            taskHistoryDataResponse.setCompletedTime(
                    LocalDateTime.parse(taskHistoryData.getCompletedTime(), DateTimeFormatter.ISO_DATE_TIME));
        }
        UserDisplayName creator = userRepository.findDisplayNameById(taskHistoryData.getCreatorId()).orElseThrow(
                () -> new EntityNotFoundException(taskHistoryData.getCreatorId(), "User")
        );
        taskHistoryDataResponse.setCreator(creator);

        UserDisplayName executor = userRepository.findDisplayNameById(taskHistoryData.getExecutorId()).orElseThrow(
                () -> new EntityNotFoundException(taskHistoryData.getExecutorId(), "User")
        );
        taskHistoryDataResponse.setExecutor(executor);

        ProjectDTO project = projectRepository.findDTOById(taskHistoryData.getProjectId()).orElseThrow(
                () -> new EntityNotFoundException(taskHistoryData.getProjectId(), "Project")
        );
        taskHistoryDataResponse.setProject(project);

        TaskSummary parentTask = null;
        if (taskHistoryData.getParentTaskId() != null) {
            parentTask = taskRepository.findSummaryById(taskHistoryData.getParentTaskId()).orElseThrow(
                    () -> new EntityNotFoundException(taskHistoryData.getParentTaskId(), "Task")
            );
        }
        taskHistoryDataResponse.setParentTask(parentTask);

        taskHistoryDataResponse.setPriority(taskHistoryData.getPriority());
        taskHistoryDataResponse.setStatus(taskHistoryData.getStatus());
        taskHistoryDataResponse.setAvailable(taskHistoryData.getAvailable());

        taskHistoryResponse.setData(taskHistoryDataResponse);

        return taskHistoryResponse;
    }
}
