package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskFileEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.forms.tasks.files.TaskFileCreateForm;
import com.fpt.cpdm.forms.tasks.files.TaskFileUpdateForm;
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
    public TaskFileDetail create(Integer taskId, TaskFileCreateForm taskFileCreateForm) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();
        LocalDateTime now = LocalDateTime.now();
        String filename = taskFileCreateForm.getFilename();
        String originalFilename = taskFileCreateForm.getFile().getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String detailFilename = task.getProject().getAlias() + "_" +
                task.getTitle() + "_" +
                currentLoggedUser.getEmail() + "_" +
                now.toString().substring(0, now.toString().indexOf("."))
                        .replaceFirst("T", " ")
                        .replaceFirst(":", "h")
                        .replaceFirst(":", "m")
                + "_" + filename + "." + extension;
        fileStorageService.store(taskFileCreateForm.getFile(), detailFilename);
        TaskFileEntity taskFileEntity = new TaskFileEntity();
        taskFileEntity.setTask(task);
        taskFileEntity.setCreator(currentLoggedUser);
        taskFileEntity.setCreatedTime(now);
        taskFileEntity.setLastEditor(currentLoggedUser);
        taskFileEntity.setLastModifiedTime(now);
        taskFileEntity.setFilename(filename);
        taskFileEntity.setDetailFilename(detailFilename);
        taskFileEntity.setExtension(extension);
        taskFileEntity.setDescription(taskFileCreateForm.getDescription());

        TaskFileEntity savedTaskFileEntity = taskFilesRepository.save(taskFileEntity);

        TaskFileDetail savedTaskFileDetail = taskFilesRepository.findDetailByIdAndAvailableTrue(savedTaskFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskFileEntity.getId(), "TaskFile")
        );

        return savedTaskFileDetail;
    }

    @Override
    public TaskFileDetail update(Integer id, TaskFileUpdateForm taskFileUpdateForm) {
        TaskFileEntity taskFileEntity = taskFilesRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "TaskFile")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();

        LocalDateTime now = LocalDateTime.now();
        String filename = taskFileUpdateForm.getFilename();

        if (taskFileUpdateForm.getFile() != null) {
            String originalFilename = taskFileUpdateForm.getFile().getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            String detailFilename = taskFileEntity.getTask().getProject().getAlias() + "_" +
                    taskFileEntity.getTask().getTitle() + "_" +
                    currentLoggedUser.getEmail() + "_" +
                    now.toString().substring(0, now.toString().indexOf("."))
                            .replaceFirst("T", " ")
                            .replaceFirst(":", "h")
                            .replaceFirst(":", "m")
                    + "_" + filename + "." + extension;
            fileStorageService.store(taskFileUpdateForm.getFile(), detailFilename);
            taskFileEntity.setExtension(extension);
            taskFileEntity.setDetailFilename(detailFilename);
        } else {
            String detailFilename = taskFileEntity.getTask().getProject().getAlias() + "_" +
                    taskFileEntity.getTask().getTitle() + "_" +
                    currentLoggedUser.getEmail() + "_" +
                    taskFileEntity.getCreatedTime().toString().substring(0,
                            taskFileEntity.getCreatedTime().toString().indexOf("."))
                            .replaceFirst("T", " ")
                            .replaceFirst(":", "h")
                            .replaceFirst(":", "m")
                    + "_" + filename + "." + taskFileEntity.getExtension();
            taskFileEntity.setDetailFilename(detailFilename);
        }

        taskFileEntity.setLastEditor(currentLoggedUser);
        taskFileEntity.setLastModifiedTime(now);
        taskFileEntity.setFilename(filename);

        taskFileEntity.setDescription(taskFileUpdateForm.getDescription());
        TaskFileEntity savedTaskFileEntity = taskFilesRepository.save(taskFileEntity);
        TaskFileDetail savedTaskFileDetail = taskFilesRepository.findDetailByIdAndAvailableTrue(savedTaskFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskFileEntity.getId(), "TaskFile")
        );
        return savedTaskFileDetail;
    }

    @Override
    public void delete(Integer id) {

        TaskFileEntity taskFileEntity = taskFilesRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "TaskFile")
        );
        taskFileEntity.setAvailable(Boolean.FALSE);
        taskFilesRepository.save(taskFileEntity);
    }

    public List<TaskFileDetail> findAllSummaryByTask_Id(Integer id) {
        return taskFilesRepository.findAllDetailByTask_IdAndAvailableTrue(id);
    }
}
