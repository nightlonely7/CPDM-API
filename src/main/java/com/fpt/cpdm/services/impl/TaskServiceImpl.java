package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.departments.DepartmentNotSameException;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskTimeException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.DepartmentRepository;
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
    private final DepartmentRepository departmentRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           DocumentRepository documentRepository,
                           DepartmentRepository departmentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public TaskDetail findDetailById(User user, Integer id) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);

        if (taskRepository.existsByCreatorOrExecutor(userEntity, userEntity) == false) {
            throw new UnauthorizedException();
        }

        TaskDetail taskDetail = taskRepository.findDetailById(id);

        return taskDetail;
    }

    @Override
    public TaskSummary changeStatus(Task task) {

        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(
                () -> new TaskNotFoundException(task.getId())
        );

        // check if executor related to task
        if (task.getExecutor().getId().equals(taskEntity.getExecutor().getId()) == false) {
            throw new UnauthorizedException();
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

        // find executor
        UserEntity executor = userRepository.findById(task.getExecutor().getId()).orElseThrow(
                () -> new UserNotFoundException(task.getExecutor().getId())
        );

        // find creator
        UserEntity creator = userRepository.findById(task.getCreator().getId()).orElseThrow(
                () -> new UserNotFoundException(task.getCreator().getId())
        );

        // check executor and creator in the same department
        if (executor.getDepartment().equals(creator.getDepartment()) == false) {
            throw new UnauthorizedException();
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
    public Page<TaskSummary> findAllSummaryByExecutor(User user, Pageable pageable) {

        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByExecutorAndIsAvailableTrue(userEntity, pageable);

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByCreator(User user, String title, String summary, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        //Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByCreator(userEntity, title, pageable);
        Page<TaskSummary> taskSummaries = taskRepository.findAllSummaryByCreatorAndTitleContainsAndSummaryContainsAndIsAvailableTrue(
                userEntity, title, summary, pageable);

        return taskSummaries;
    }

    @Override
    public void deleteById(Integer id) {

        TaskEntity taskEntity = taskRepository.findById(id).get();
        taskEntity.setIsAvailable(false);
        taskRepository.save(taskEntity);
    }

}
