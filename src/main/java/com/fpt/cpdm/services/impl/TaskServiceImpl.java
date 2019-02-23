package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskTimeException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
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
    public TaskSummary save(Task task) {

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

        // check documents exist (can be null)
        if (task.getDocuments() != null && task.getDocuments().isEmpty() == false) {
            for (Document document : task.getDocuments()) {
                if (documentRepository.existsById(document.getId()) == false) {
                    throw new DocumentNotFoundException(document.getId());
                }
            }
        }

        TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        TaskSummary savedTaskSummary = taskRepository.findSummaryById(savedTaskEntity.getId());

        return savedTaskSummary;
    }

    @Override
    public List<TaskSummary> findAllSummary() {

        List<TaskSummary> taskSummaries = taskRepository.findAllSummaryBy();

        return taskSummaries;
    }

    @Override
    public List<TaskSummary> findAllSummaryByExecutor(User user) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        List<TaskSummary> taskSummaries = taskRepository.findAllSummaryByExecutor(userEntity);

        return taskSummaries;
    }

    @Override
    public List<TaskSummary> findAllSummaryByCreator(User user) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        List<TaskSummary> taskSummaries = taskRepository.findAllSummaryByCreator(userEntity);

        return taskSummaries;
    }

}
