package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.exceptions.tasks.TaskIdNotFoundException;
import com.fpt.cpdm.models.Task;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {

        // check id exist
        if (task.getId() != null && taskRepository.existsById(task.getId()) == false) {
            throw new TaskIdNotFoundException(task.getId());
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
