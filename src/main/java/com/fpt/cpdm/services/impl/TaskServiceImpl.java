package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskTimeException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           DocumentRepository documentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public Task save(Task task) {

        // check task exist (can be null)
        if (task.getId() != null && taskRepository.existsById(task.getId()) == false) {
            throw new TaskNotFoundException(task.getId());
        }

        // check end time after start time
        if (task.getEndTime().isBefore(task.getStartTime())) {
            throw new TaskTimeException("End time is before start time!");
        }

        // check creator exists
        if (userRepository.existsById(task.getCreator().getId()) == false) {
            throw new UserNotFoundException(task.getCreator().getId());
        }

        // check executor exists
        if (userRepository.existsById(task.getExecutor().getId()) == false) {
            throw new UserNotFoundException(task.getExecutor().getId());
        }

        // check parent task exists (can be null)
        if (task.getParentTask() != null && taskRepository.existsById(task.getParentTask().getId()) == false) {
            throw new TaskNotFoundException(task.getParentTask().getId());
        }

        // check documents exist
        for (Document document : task.getDocuments()) {
            if (documentRepository.existsById(document.getId()) == false) {
                throw new DocumentNotFoundException(document.getId());
            }
        }

        TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        Task savedTask = ModelConverter.taskEntityToModel(savedTaskEntity);

        return savedTask;
    }

    @Override
    public List<Task> saveAll(List<Task> tasks) {
        // TODO
        return null;
    }

    @Override
    public Task findById(Integer id) {
        // TODO
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        // TODO
        return false;
    }

    @Override
    public List<Task> findAll() {
        List<TaskEntity> taskEntities = taskRepository.findAll();

        List<Task> tasks = getTasksConverted(taskEntities);
        return tasks;
    }

    @Override
    public List<TaskSummary> findAllSummary() {
        List<TaskSummary> taskSummaries = taskRepository.findAllSummaryBy();
        return taskSummaries;
    }

    @Override
    public List<Task> findAllById(List<Integer> ids) {
        // TODO
        return null;
    }

    @Override
    public long count() {
        // TODO
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        // TODO
    }

    @Override
    public void delete(Task task) {
        // TODO
    }

    @Override
    public void deleteAll(List<Task> tasks) {
        // TODO
    }

    @Override
    public void deleteAll() {
        // TODO
    }

    private List<Task> getTasksConverted(List<TaskEntity> taskEntities) {
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            Task task = ModelConverter.taskEntityToModel(taskEntity);
            tasks.add(task);
        }
        return tasks;
    }

    private List<TaskEntity> getTaskEntityConverted(List<Task> tasks) {
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Task task : tasks) {
            TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }


}
