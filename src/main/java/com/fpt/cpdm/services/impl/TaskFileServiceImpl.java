package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskFileEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.models.tasks.task_files.TaskFileSummary;
import com.fpt.cpdm.repositories.TaskFilesRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.FileStorageService;
import com.fpt.cpdm.services.TaskFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskFileServiceImpl implements TaskFileService {

    private final TaskRepository taskRepository;
    private final TaskFilesRepository taskFilesRepository;
    private final FileStorageService fileStorageService;
    private final AuthenticationService authenticationService;

    @Autowired
    public TaskFileServiceImpl(TaskRepository taskRepository,
                               TaskFilesRepository taskFilesRepository,
                               FileStorageService fileStorageService,
                               AuthenticationService authenticationService) {
        this.taskRepository = taskRepository;
        this.taskFilesRepository = taskFilesRepository;
        this.fileStorageService = fileStorageService;
        this.authenticationService = authenticationService;
    }

    @Override
    public TaskFileSummary create(Integer taskId, MultipartFile file) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = task.getTitle() + "_" +
                task.getProject().getAlias() + "_" +
                currentLoggedUser.getEmail() + "_" +
                LocalDateTime.now().toString()
                        .replaceFirst("T", " ")
                        .replaceFirst(":", "h")
                        .replaceFirst(":", "m")
                + "_" + filename;

        fileStorageService.store(file, newFileName);
        TaskFileEntity taskFileEntity = new TaskFileEntity();
        taskFileEntity.setTask(task);
        taskFileEntity.setCreator(currentLoggedUser);
        taskFileEntity.setFilename(newFileName);
        TaskFileEntity savedTaskFileEntity = taskFilesRepository.save(taskFileEntity);
        TaskFileSummary savedTaskFileSummary = taskFilesRepository.findSummaryById(savedTaskFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskFileEntity.getId(), "TaskFiles")
        );
        return savedTaskFileSummary;
    }

    public List<TaskFileSummary> findSummaryByTask_Id(Integer id) {
        return taskFilesRepository.findAllSummaryByTask_Id(id);
    }
}
