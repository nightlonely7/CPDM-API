package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
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
    public Task save(Task entity) {
        return null;
    }

    @Override
    public List<Task> saveAll(List<Task> entities) {
        return null;
    }

    @Override
    public Task findById(Integer id) {
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
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
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void delete(Task entity) {

    }

    @Override
    public void deleteAll(List<Task> entities) {

    }

    @Override
    public void deleteAll() {

    }

    private List<Task> getTasksConverted(List<TaskEntity> taskEntities) {
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            Task task = ModelConverter.taskEntityToModel(taskEntity);
            tasks.add(task);
        }
        return tasks;
    }

    private List<TaskEntity> getTaskEntityConverted(List<Task> tasks){
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Task task : tasks) {
            TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }
}
