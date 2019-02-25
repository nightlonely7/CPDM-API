package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.UnauthorizedException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public TaskSummary changeStatus(Task task) {

        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(
                () -> new TaskNotFoundException(task.getId())
        );

        // check if executor related to task
        if (task.getExecutor().getId().equals(taskEntity.getExecutor().getId()) == false) {
            throw new UnauthorizedException("This user is not allow to change this task");
        }

        taskEntity.setStatus(task.getStatus());
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        TaskSummary savedTaskSummary = taskRepository.findSummaryById(savedTaskEntity.getId());

        return savedTaskSummary;
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
    public Page<TaskSummary> findAllSummary(Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryBy(pageable);

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutor(User user, Pageable pageable) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByExecutor(userEntity, pageable);

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByCreator(User user, Pageable pageable) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByCreator(userEntity, pageable);

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndTitleContaining(User user, String title, Pageable pageable) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByExecutorAndTitleContaining(userEntity, title, pageable);

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByCreatorAndTitleContaining(User user, String title, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByCreatorAndTitleContaining(userEntity, title, pageable);

        return taskSummaries;
    }

}
