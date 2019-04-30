package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskFileEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.forms.tasks.files.TaskFileForm;
import com.fpt.cpdm.models.tasks.task_files.TaskFileDetail;
import com.fpt.cpdm.repositories.TaskFilesRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.FileStorageService;
import com.fpt.cpdm.services.TaskFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public TaskFileDetail create(Integer taskId, TaskFileForm taskFileForm) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();
        String originalFilename = taskFileForm.getFile().getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.indexOf(".") + 1);
        String filename = taskFileForm.getFilename();
        String detailFilename = task.getProject().getAlias() + "_" +
                task.getTitle() + "_" +
                currentLoggedUser.getEmail() + "_" +
                LocalDateTime.now().toString()
                        .replaceFirst("T", " ")
                        .replaceFirst(":", "h")
                        .replaceFirst(":", "m")
                + "_" + filename + "." + extension;
        fileStorageService.store(taskFileForm.getFile(), detailFilename);
        TaskFileEntity taskFileEntity = new TaskFileEntity();
        taskFileEntity.setTask(task);
        taskFileEntity.setCreator(currentLoggedUser);
        taskFileEntity.setFilename(filename);
        taskFileEntity.setDetailFilename(detailFilename);
        taskFileEntity.setExtension(extension);
        taskFileEntity.setDescription(taskFileForm.getDescription());
        TaskFileEntity savedTaskFileEntity = taskFilesRepository.save(taskFileEntity);
        TaskFileDetail savedTaskFileDetail = taskFilesRepository.findDetailById(savedTaskFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskFileEntity.getId(), "TaskFiles")
        );
        return savedTaskFileDetail;
    }

    public List<TaskFileDetail> findSummaryByTask_Id(Integer id) {
        return taskFilesRepository.findAllDetailByTask_Id(id);
    }
}
