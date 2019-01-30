package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.Task;
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

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task save(Task task) {

        // check id exist
        if (task.getId() != null && taskRepository.existsById(task.getId()) == false) {
            throw new TaskNotFoundException(task.getId());
        }

        // check creator
        if (task.getCreator() == null || userRepository.existsById(task.getCreator().getId()) == false) {
            throw new ModelNotValidException("Creator not found on task");
        }

        TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);

        // set creator
        UserEntity creatorEntity = userRepository.findById(task.getCreator().getId()).orElseThrow(
                () -> new UserNotFoundException(task.getCreator().getId())
        );
        taskEntity.setCreator(creatorEntity);

        // set parent task
        if (task.getParentTask() != null) {
            TaskEntity parentTaskEntity = taskRepository.findById(task.getParentTask().getId()).orElseThrow(
                    () -> new TaskNotFoundException(task.getParentTask().getId())
            );
            taskEntity.setParentTask(parentTaskEntity);
        }

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
